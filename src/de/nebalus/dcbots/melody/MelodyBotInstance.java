package de.nebalus.dcbots.melody;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;

import de.nebalus.dcbots.melody.audioplayer.GuildAudioController;
import de.nebalus.dcbots.melody.audioplayer.MusicManager;
import de.nebalus.dcbots.melody.audioplayer.handler.AudioLoadResult;
import de.nebalus.framework.gfw.modules.dcbot.api.DCBotInstance;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class MelodyBotInstance extends DCBotInstance {

	private final MelodyApp melodyApp;
	
	private AudioPlayerManager audioPlayerManager;
	private MusicManager musicManager;
	
	public MelodyBotInstance(MelodyApp melodyApp) {
		this.melodyApp = melodyApp;
	}

	@Override
	protected void onLoad() throws Exception {
		
		melodyApp.getLogger().logInfo("Building JDA instance...");
		
		JDABuilder builder = JDABuilder.create(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES);

		builder.setToken("");
		builder.setStatus(OnlineStatus.OFFLINE);
		builder.setActivity(Activity.playing("Relaunch coming soon!"));
		builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.ONLINE));
		builder.setChunkingFilter(ChunkingFilter.NONE);
		builder.setLargeThreshold(50);
		builder.addEventListeners();

		finalizeJDABuildProcess(builder);

		melodyApp.getLogger().logInfo("JDA Succesfully build");
		
		audioPlayerManager = new DefaultAudioPlayerManager();
		musicManager = new MusicManager(this);
		
		// Print some information about the bot
//		melodyApp.getLogger().log("This bot is connected as '" + getJDA().getSelfUser().getAsTag() + "'");
//		melodyApp.getLogger().log("The bot is present on these guilds:");
//		for (Guild guild : getJDA().getGuildCache()) {
//			melodyApp.getLogger().log("\t- " + guild.getName() + " (" + guild.getId() + ") (" + guild.getMembers().size() + " Members)");
//			for (Member member : guild.getMembers()) {
//				melodyApp.getLogger().log("\t\t- " + member.getEffectiveName() + " ~ " + member.getIdLong());
//			}
//		}
		
		long testGuild = 958332676023128095l;
		long testVc = 1035155614566977576l;
		Guild guild = getJDA().getGuildById(testGuild);
		VoiceChannel vc = guild.getVoiceChannelById(testVc);
		
		AudioManager manager = guild.getAudioManager();
		manager.setSelfDeafened(true);
		manager.openAudioConnection(vc);
		
		GuildAudioController gac = musicManager.getController(testGuild);
		AudioLoadResult alr = new AudioLoadResult(gac);
		
		String input = "Very True, But Will Give".trim();
		
		//audioPlayerManager.loadItem("https://youtu.be/nwF3Kp6d-ls", alr);
		audioPlayerManager.loadItem(input + "ytsearch:", alr);
		//audioPlayerManager.loadItem("https://soundcloud.com/sweeetsour/rebuke-anyma-syren-extended-mix-afterlife", alr);
		//audioPlayerManager.loadItem(refrence, alr);
		//audioPlayerManager.loadItem("https://www.twitch.tv/bastighg", alr);
		
		TimeUnit.SECONDS.sleep(5);
		
		manager.closeAudioConnection();
		
		unload();
	}

	@Override
	protected void onUnload() throws Exception {
		// Shutdowns the Audio Client
		audioPlayerManager.shutdown();
		
		// Shutdowns the JDA Bot Client
		JDA jda = getJDA();
		if (jda != null) {
			jda.shutdown();
			if (!jda.awaitShutdown(Duration.ofSeconds(10))) {
				jda.shutdownNow();
				jda.awaitShutdown();
			}
		}
	}

	public AudioPlayerManager getAudioPlayerManager() {
		return audioPlayerManager;
	}
	
	public MusicManager getMusicManager() {
		return musicManager;
	}
}

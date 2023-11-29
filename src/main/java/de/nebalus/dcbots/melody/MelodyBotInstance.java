package de.nebalus.dcbots.melody;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import de.nebalus.dcbots.melody.audioplayer.GuildAudioController;
import de.nebalus.dcbots.melody.audioplayer.MusicManager;
import de.nebalus.dcbots.melody.audioplayer.handler.AudioLoadResult;
import de.nebalus.framework.gfw.modules.dcbot.api.DCBotInstance;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

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

		JDABuilder builder = JDABuilder.create(
				GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES);

		builder.setToken("");
		builder.disableCache(CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS);
		builder.setStatus(OnlineStatus.OFFLINE);
		builder.setActivity(Activity.playing("Relaunch coming soon!"));
		builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.ONLINE));
		builder.setChunkingFilter(ChunkingFilter.NONE);
		builder.setLargeThreshold(50);
		builder.addEventListeners();
		finalizeJDABuildProcess(builder);

        getJDA().getRestPing().queue(ping ->
        	melodyApp.getLogger().logInfo("Logged in with ping: " + ping)
        );
		
		getJDA().awaitReady();
		
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
/*
		Long testGuild = 958332676023128095l;
		Long testVc = 1065300858193068052l;
		Guild guild = getJDA().getGuildById(testGuild);
		VoiceChannel vc = guild.getVoiceChannelById(testVc);

		AudioManager manager = guild.getAudioManager();
		manager.setSelfDeafened(true);
		manager.openAudioConnection(vc);

		GuildAudioController gac = musicManager.getController(testGuild);
		AudioLoadResult alr = new AudioLoadResult(gac);

		String input = "Very True, But Will Give".trim();

		audioPlayerManager.loadItem("https://youtu.be/nwF3Kp6d-ls", alr);
		// audioPlayerManager.loadItem(input + "ytsearch:", alr);
		// audioPlayerManager.loadItem("https://soundcloud.com/sweeetsour/rebuke-anyma-syren-extended-mix-afterlife",
		// alr);
		// audioPlayerManager.loadItem(refrence, alr);
		// audioPlayerManager.loadItem("https://www.twitch.tv/bastighg", alr);
*/
		
		melodyApp.getLogger().logInfo("Melody loaded");

		TimeUnit.SECONDS.sleep(5);

		unload();
	}

	@Override
	protected void onUnload() throws Exception {
		// Shutdowns the Audio Client
		audioPlayerManager.shutdown();

		// Shutdowns the JDA Bot Client
		JDA jda = getJDA();
		if (jda != null) {
			melodyApp.getLogger().logInfo("Shutingdown JDA instance...");
			jda.shutdown();
			if (!jda.awaitShutdown(Duration.ofSeconds(10))) {
				melodyApp.getLogger().logWarning("JDA took to long to shutdown. Executing force shutdown");
				jda.shutdownNow();
				jda.awaitShutdown();
			}
		}

		melodyApp.getLogger().logInfo("Melody unloaded");
	}

	public AudioPlayerManager getAudioPlayerManager() {
		return audioPlayerManager;
	}

	public MusicManager getMusicManager() {
		return musicManager;
	}
}

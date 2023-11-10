package de.nebalus.dcbots.melody;

import java.time.Duration;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;

import de.nebalus.dcbots.melody.audioplayer.AudioManager;
import de.nebalus.framework.gfw.modules.dcbot.api.DCBotInstance;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class MelodyBotInstance extends DCBotInstance {

	private final MelodyApp melodyApp;
	
	private AudioPlayerManager audioPlayerManager;
	private AudioManager audioManager;
	
	public MelodyBotInstance(MelodyApp melodyApp) {
		this.melodyApp = melodyApp;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onLoad() throws Exception {
		
		melodyApp.getLogger().logInfo("Building JDA instance...");
		
		JDABuilder builder = JDABuilder.create(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES);

		builder.setToken("ODAxODU2Njc4MDYzODk4NjQ0.GZVAIF.0oz2ZAiXQlu0LpYGfUOzS1_qRM_O_BCAi5wquo");
		builder.setStatus(OnlineStatus.OFFLINE);
		builder.setActivity(Activity.playing("Relaunch coming soon!"));
		builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.ONLINE));
		builder.setChunkingFilter(ChunkingFilter.NONE);
		builder.setLargeThreshold(50);
		builder.addEventListeners();

		finalizeJDABuildProcess(builder);

		melodyApp.getLogger().logInfo("JDA Succesfully build");
		
		audioPlayerManager = new DefaultAudioPlayerManager();
		audioManager = new AudioManager(this);
		
		// Print some information about the bot
		melodyApp.getLogger().log("This bot is connected as '" + getJDA().getSelfUser().getAsTag() + "'");
		melodyApp.getLogger().log("The bot is present on these guilds:");
		for (Guild guild : getJDA().getGuildCache()) {
			melodyApp.getLogger().log("\t- " + guild.getName() + " (" + guild.getId() + ") (" + guild.getMembers().size() + " Members)");
			for (Member member : guild.getMembers()) {
				melodyApp.getLogger().log("\t\t- " + member.getEffectiveName() + " ~ " + member.getIdLong());
			}
		}
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
	
	public AudioManager getAudioManager() {
		return audioManager;
	}
}

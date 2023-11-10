package de.nebalus.dcbots.melody;

import java.time.Duration;

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

	public MelodyBotInstance() {

	}

	@Override
	protected void onLoad() throws Exception {

		JDABuilder builder = JDABuilder.create(
				GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES);

		builder.setToken("");
		builder.setStatus(OnlineStatus.OFFLINE);
		builder.setActivity(Activity.playing("Relaunch coming soon!"));
		builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.ONLINE));
		builder.setChunkingFilter(ChunkingFilter.NONE);
		builder.setLargeThreshold(50);
		builder.addEventListeners();

		finalizeJDABuildProcess(builder);
		
		MelodyCore.getMelodyApp().getLogger().log("Melodys connection was succesfully build");

		// Print some information about the bot
		MelodyCore.getMelodyApp().getLogger().log("Melody is connected as '" + getJDA().getSelfUser().getEffectiveName() + "'");
		MelodyCore.getMelodyApp().getLogger().log("The bot is present on these guilds:");
		for (Guild guild : getJDA().getGuildCache()) {
			MelodyCore.getMelodyApp().getLogger().log("\t- " + guild.getName() + " (" + guild.getId() + ") (" + guild.getMembers().size() + " Members)");
			for(Member member : guild.getMembers()) {
				MelodyCore.getMelodyApp().getLogger().log("\t\t- " + member.getEffectiveName());
			}
		}
	}

	@Override
	protected void onUnload() throws Exception {
		JDA jda = getJDA();
		if(jda != null) {
			 jda.shutdown();
			 if (!jda.awaitShutdown(Duration.ofSeconds(10))) {
			     jda.shutdownNow();
			     jda.awaitShutdown();
			 }
		}
	}
}

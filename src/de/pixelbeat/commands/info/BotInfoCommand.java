package de.pixelbeat.commands.info;


import java.text.SimpleDateFormat;
import java.util.Date;

import de.pixelbeat.Json;
import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class BotInfoCommand implements ServerCommand{

	private int membersDeserving = 0;

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		
		int serversRunning = channel.getJDA().getGuilds().size(); 
		for(Guild g1 : channel.getJDA().getGuilds()) {
			membersDeserving = membersDeserving + g1.getMemberCount();
		}
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(0x23cba7);
		builder.setThumbnail(guild.getSelfMember().getUser().getAvatarUrl());
		builder.setDescription(PixelBeat.INSTANCE.getMessageFormatter().format(guild.getIdLong(), "feedback.info.botinfo",
			"JDA",
			PixelBeat.INSTANCE.version,
			serversRunning,
			membersDeserving,
			Utils.uptime(PixelBeat.INSTANCE.uptime),
			botstart(),
			Utils.uptime(Json.getTotalOnlineTime()),
			Utils.getUserInt(),
			Utils.uptime(Json.getPlayedMusicTime()),
			guild.getSelfMember().getAsMention()));
		builder.setFooter("Made by Nebalus#1665 with <3");
		channel.sendMessage(builder.build()).queue();
		membersDeserving = 0;
	}
	
	public int getCooldown() {
		return 5;
	}
	
	public String botstart() {
		Date date = new Date(PixelBeat.INSTANCE.startuptime);
		String DateFormat = new SimpleDateFormat("EEE, d MMM HH:mm:ss yyyy").format(date);
		return DateFormat;
	}
}

package de.pixelbeat.commands.info;


import java.text.SimpleDateFormat;
import java.util.Date;

import de.pixelbeat.Json;
import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.utils.Misc;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class BotInfoCommand implements ServerCommand{

	private int membersDeserving = 0;
	

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		Guild guild = channel.getGuild();
		
		int serversRunning = channel.getJDA().getGuilds().size(); 
		for(Guild g1 : channel.getJDA().getGuilds()) {
			membersDeserving = membersDeserving + g1.getMemberCount();
		}
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(0x23cba7);
		builder.setThumbnail(guild.getSelfMember().getUser().getAvatarUrl());
		builder.setDescription("Framework: JDA\n"
				+ "Version: "+PixelBeat.version+"\n"
				+ "Running on servers: "+serversRunning+"\n"
				+ "Deserving members: "+membersDeserving+"\n"
				+ "Uptime: "+Misc.uptime(PixelBeat.uptime)+"\n"
				+ "Bot-Start: "+botstart()+"\n"
				+ "Totaltimeonline: "+Misc.uptime(Json.getTotalOnlineTime())+"\n"
				+ " \n"
				+ "At the time you look at this, "+Misc.getUserInt()+" people listened for "+Misc.uptime(Json.getPlayedMusicTime())+" to music thru "+channel.getGuild().getSelfMember().getAsMention()+"!\n");
		builder.setFooter("Made by Tyten#1665 with <3");
		channel.sendMessage(builder.build()).queue();
		membersDeserving = 0;
	}

	
	public static String botstart() {
		Date date = new Date(PixelBeat.startuptime);
		String DateFormat = new SimpleDateFormat("EEE, d MMM HH:mm:ss yyyy").format(date);
		return DateFormat;
	}
}

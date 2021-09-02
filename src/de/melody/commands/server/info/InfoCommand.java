package de.melody.commands.server.info;


import java.util.List;
import java.util.Properties;

import de.melody.CommandManager.CommandType;
import de.melody.commands.types.ServerCommand;
import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class InfoCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		channel.sendMessageEmbeds(getInfoEmbed(guild).build()).queue();
	}
	
	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		event.replyEmbeds(getInfoEmbed(guild).build()).queue();
	}

	private EmbedBuilder getInfoEmbed(Guild guild) {
		int serversRunning = guild.getJDA().getGuilds().size(); 
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Constants.EMBEDCOLOR);
		builder.setThumbnail(guild.getJDA().getSelfUser().getEffectiveAvatarUrl());
		
		Runtime r = Runtime.getRuntime();
		Properties prop = System.getProperties();
		String smallmemory = new String(r.totalMemory()+"");
		String bigmemory = new String(r.totalMemory()/ 1048576+"");
		
		builder.setDescription(Melody.INSTANCE.getMessageFormatter().format(guild, "feedback.info.botinfo",
			"JDA",
			Constants.BUILDVERSION,
			Constants.BUILDDATE,
			serversRunning,
			Utils.getUserInt(),
			Utils.decodeStringFromTimeMillis(Melody.INSTANCE.playedmusictime,true),
			guild.getSelfMember().getAsMention())
				
			+" \n \n```OS: "+prop.getProperty("os.name")+"\n"
			+ "Cores: "+r.availableProcessors()+"\n"
			+ "CPU Arch: "+prop.getProperty("os.arch")+"\n"
			+ "Memory Usage: "+bigmemory+"."+smallmemory.substring(bigmemory.length())+"MB\n"
			+ "Uptime: "+Utils.decodeStringFromTimeMillis(Melody.INSTANCE.uptime,true)+"```");
		builder.setFooter("Made by Nebalus#1665 with <3");
		
		return builder;
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("info");
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.INFO_COMMAND;
	}

	@Override
	public boolean isSlashCommandCompatible() {
		return true;
	}
	
	@Override
	public String getCommandDescription() {
		return "Show some information about "+Constants.BUILDNAME;
	}
}

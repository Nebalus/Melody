package de.melody.commands.info;


import java.util.List;
import java.util.Properties;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.utils.Utils;
import de.melody.utils.commandbuilder.CommandInfo;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import de.melody.utils.messenger.Messenger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class InfoCommand implements ServerCommand{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild) {
		Messenger.sendMessageEmbed(channel, getInfoEmbed(guild)).queue();
	}
	
	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		event.replyEmbeds(Messenger.getMessageEmbed(getInfoEmbed(guild))).queue();
	}

	private EmbedBuilder getInfoEmbed(Guild guild) {
		int serversRunning = guild.getJDA().getGuilds().size(); 
		EmbedBuilder builder = new EmbedBuilder();
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
			Utils.decodeStringFromTimeMillis(Utils.getAllUsersHeardTimeInt(),true),
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
		return CommandType.BOTH;
	}

	@Override
	public String getCommandDescription() {
		return "Show some information about "+Constants.BUILDNAME;
	}

	@Override
	public List<OptionData> getCommandOptions() {
		return null;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.INFO_COMMAND;
	}
}

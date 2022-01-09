package de.melody.commands.info;


import java.util.Properties;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.tools.Utils;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.Command;
import de.melody.tools.helper.MathHelper;
import de.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class InfoCommand implements Command{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		Messenger.sendMessageEmbed(channel, getInfoEmbed(guild)).queue();
	}
	
	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		event.replyEmbeds(Messenger.getMessageEmbed(getInfoEmbed(guild))).queue();
	}

	private EmbedBuilder getInfoEmbed(Guild guild) {
		int serversRunning = guild.getJDA().getGuilds().size(); 
		EmbedBuilder builder = new EmbedBuilder();
		builder.setThumbnail(Constants.ICON_URL);
		
		Runtime r = Runtime.getRuntime();
		Properties prop = System.getProperties();
		String smallmemory = new String(r.totalMemory()+"");
		String bigmemory = new String(r.totalMemory()/ 1048576+"");
		
		builder.setDescription(Melody.INSTANCE._messageformatter.format(guild, "feedback.info.botinfo",
			"JDA",
			Constants.BUILDVERSION,
			Constants.BUILDDATE,
			serversRunning,
			Utils.getUserInt(),
			MathHelper.decodeStringFromTimeMillis(Utils.getAllUsersHeardTimeSec()*1000),
			guild.getSelfMember().getAsMention())
				
			+" \n \n```OS: "+prop.getProperty("os.name")+"\n"
			+ "Cores: "+r.availableProcessors()+"\n"
			+ "CPU Arch: "+prop.getProperty("os.arch")+"\n"
			+ "Memory Usage: "+bigmemory+"."+smallmemory.substring(bigmemory.length())+"MB\n"
			+ "Uptime: "+MathHelper.decodeStringFromTimeMillis(System.currentTimeMillis() - Melody.INSTANCE._startupmillis)+"```");
		builder.setFooter("Made by Nebalus#1665 with <3");
		
		return builder;
	}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"info"};
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
	public OptionData[] getCommandOptions() {
		return null;
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.EVERYONE;
	}
}

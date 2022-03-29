package de.melody.commands.info;

import java.util.Properties;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.tools.Utils;
import de.melody.tools.cmdbuilder.CommandPermission;
import de.melody.tools.cmdbuilder.CommandType;
import de.melody.tools.cmdbuilder.ServerCommand;
import de.melody.tools.entitymanager.entitys.GuildEntity;
import de.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class InfoCommand extends ServerCommand {

	public InfoCommand() {
		super();
		setPrefixes("info");
		setDescription("Shows som information about "+Constants.BUILDNAME);
		setMainPermission(CommandPermission.EVERYONE);
		setType(CommandType.CHAT);
	}
	
	@Override
	public void performMainCMD(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		Messenger.sendMessageEmbed(channel, getInfoEmbed(guild)).queue();
	}
	
	private EmbedBuilder getInfoEmbed(Guild guild) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setThumbnail(Constants.ICON_URL);
		
		Runtime r = Runtime.getRuntime();
		Properties prop = System.getProperties();
		String smallmemory = new String(r.totalMemory()+"");
		String bigmemory = new String(r.totalMemory()/ 1048576+"");
		
		builder.setDescription(
			"\n```OS: "+prop.getProperty("os.name")+"\n"
			+ "Cores: "+r.availableProcessors()+"\n"
			+ "CPU Arch: "+prop.getProperty("os.arch")+"\n"
			+ "Memory Usage: "+bigmemory+"."+smallmemory.substring(bigmemory.length())+"MB```");
	 	builder.setFooter("Made by Nebalus#1665 with <3");
		
		return builder;
	}
}

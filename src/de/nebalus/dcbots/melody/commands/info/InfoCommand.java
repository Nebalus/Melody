package de.nebalus.dcbots.melody.commands.info;

import java.util.Properties;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.cmdbuilder.CommandPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.embedbuilders.DefaultEmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class InfoCommand extends ServerCommand
{

	public InfoCommand() 
	{
		super("info");
		setMainPermission(CommandPermission.EVERYONE);
		setDescription("Shows some information about " + Build.NAME + ".");
	}
	
	@Override
	public void performMainCmd(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) 
	{
		final int serversrunning = guild.getJDA().getGuilds().size(); 			
		final Runtime runtime = Runtime.getRuntime();
		final Properties prop = System.getProperties();
		final String smallmemory = new String(runtime.totalMemory() + "");
		final String bigmemory = new String(runtime.totalMemory() / 1048576 + "");
		final DefaultEmbedBuilder builder = new DefaultEmbedBuilder();	
		
		builder.setThumbnail(Url.ICON.toString());
		builder.setDescription(Melody.formatMessage(guild, "feedback.info.botinfo",
			"JDA",
			Build.VERSION,
			Build.DATE,
			serversrunning,
			"UNKNOWN",
			"UNKNOWN",
			guild.getSelfMember().getAsMention())
					
			+" \n \n```OS: "+prop.getProperty("os.name")+"\n"
			+ "Cores: "+runtime.availableProcessors()+"\n"
			+ "CPU Arch: "+prop.getProperty("os.arch")+"\n"
			+ "Memory Usage: "+bigmemory+"."+smallmemory.substring(bigmemory.length())+"MB\n"
			+ "Uptime: UNKNOWN```");
		builder.setFooter("Made by " + Build.AUTHOR + " with <3");
		
		event.replyEmbeds(builder.build()).setEphemeral(true).queue();
	}
}

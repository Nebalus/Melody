package de.nebalus.dcbots.melody.commands.info;

import java.util.Properties;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.embedbuilders.DefaultEmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class InfoCommand extends SlashCommand
{

	public InfoCommand() 
	{
		super("info");
		setPermissionGroup(PermissionGroup.EVERYONE);
		setDescription("Shows some information about " + Build.NAME + ".");
		
		setExecuter(new SlashExecuter()
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook) 
			{
				final int serversrunning = guild.getJDA().getGuilds().size(); 			
				final Runtime runtime = Runtime.getRuntime();
				final Properties prop = System.getProperties();
				final String smallmemory = new String(runtime.totalMemory() + "");
				final String bigmemory = new String(runtime.totalMemory() / 1048576 + "");
				final DefaultEmbedBuilder builder = new DefaultEmbedBuilder();	
				
				builder.setThumbnail(Url.ICON.toString());
				builder.setDescription(Melody.formatMessage(guild, "command.info.info",
					"JDA",
					Build.VERSION,
					Build.DATE,
					serversrunning,
					"UNKNOWN",
					"UNKNOWN",
					guild.getSelfMember().getAsMention(),
					prop.getProperty("os.name"),
					runtime.availableProcessors(),
					prop.getProperty("os.arch"),
					bigmemory + "." + smallmemory.substring(bigmemory.length()),
					"UNKNOWN"));
				builder.setFooter("Made by " + Build.AUTHOR + " with <3");
				
				event.replyEmbeds(builder.build()).setEphemeral(true).queue();
			}
		});
	}
}

package de.nebalus.dcbots.melody.interactions.commands.info;

import java.util.Properties;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.Utils;
import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.Messenger;
import de.nebalus.dcbots.melody.tools.messenger.embedbuilders.RemasteredEmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class InfoCommand extends SlashCommand
{

	public InfoCommand() 
	{
		super("info");
		setPermissionGroup(PermissionGroup.EVERYONE);
		setDescription("Shows some information about " + Build.NAME + ".");
		
		setExecuter(new SlashInteractionExecuter()
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) 
			{
				final Runtime runtime = Runtime.getRuntime();
				final Properties prop = System.getProperties();
				final String smallmemory = runtime.totalMemory() + "";
				final String bigmemory = runtime.totalMemory() / 1048576 + "";
				
				final RemasteredEmbedBuilder builder = new RemasteredEmbedBuilder();	
				
				builder.setThumbnail(Url.ICON.toString());
				builder.setColorScheme(Settings.EMBED_COLOR);
				builder.setHeader(Build.NAME + " Information");
				builder.enableColorLine();
				builder.setBody(Melody.formatMessage(guild, "command.info.info",
					"Java JDA",
					Build.VERSION,
					Build.DATE,
					prop.getProperty("os.name"),
					runtime.availableProcessors(),
					prop.getProperty("os.arch"),
					bigmemory + "." + smallmemory.substring(bigmemory.length()),
					Utils.decodeStringFromTimeMillis(System.currentTimeMillis() - Melody.getStartUpTimeStamp())));
				builder.setFooter("Made by " + Build.AUTHOR + " with <3");
				
				Messenger.sendInteractionMessage(event, builder.build(), true);
			}
		});
	}
}
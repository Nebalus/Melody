package de.nebalus.dcbots.melody.tools.cmdbuilder;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.tools.ConsoleLogger;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public final class CommandManager 
{

	private final ConcurrentHashMap<String, ServerCommand> commandlist;

	public CommandManager(Melody melody) 
	{
		this.commandlist = new ConcurrentHashMap<String, ServerCommand>();
	}

	public final void registerCommands(ServerCommand... cmd) 
	{

		for (ServerCommand sc : cmd) 
		{
			ConsoleLogger.debug("CMD-BUILDER", "Loading CMD... | " + "PREFIX: " + sc.getPrefix());
		
			commandlist.put(sc.getPrefix().toLowerCase(), sc);

//			slashcommands.add(Commands.slash(sc.getPrefix(), sc.getDescription())
//					.addSubcommands(new SubcommandData("test", "Test")
//							.addOptions(new OptionData(OptionType.INTEGER, "requierd_autocompleteno", "?", true, false)
//									.addChoice("choice0", 0L)
//									.addChoice("choice1", 1L)
//									.addChoice("choice2", 2L)
//									.addChoice("choice3", 3L)
//									)
//							.addOption(OptionType.INTEGER, "requierd", "??", true, false)
//							.addOption(OptionType.INTEGER, "autocomplete", "???", false, true)
//							.addOption(OptionType.INTEGER, "none", "????", false, false))
//					.addSubcommands(new SubcommandData("test1", "Test1"))
//					.addSubcommandGroups(new SubcommandGroupData("lul", "Krasse gruppe")
//							.addSubcommands(new SubcommandData("omegalul", "oooomeeegalul"))));
		}

		//Exports all registered Slashcommands to the Discord Shards
		if (Melody.getConfig()._allowslashcommands) 
		{
			for (JDA jda : Melody.getShardManager().getShards()) 
			{
				CommandListUpdateAction slashupdater = jda.updateCommands();
				for (ServerCommand sc : cmd) 
				{
					slashupdater.addCommands(sc.getSlashCommandData());
				}
				slashupdater.queue();
				ConsoleLogger.debug("SLASH-BUILDER", "EXPORT generated Slash commands to Shard [ID:" + jda.getShardInfo().getShardId() + "]");
			}
		}
	}

	public final ArrayList<ServerCommand> getCommands() 
	{
		final ArrayList<ServerCommand> rawcmd = new ArrayList<ServerCommand>();
		for (Map.Entry<String, ServerCommand> entry : commandlist.entrySet()) 
		{
			rawcmd.add(entry.getValue());
		}
		return rawcmd;
	}

	public final ServerCommand getCommand(String prefix) throws NullPointerException 
	{
		final String lower_prefix = prefix.toLowerCase();
		if (this.commandlist.containsKey(lower_prefix)) 
		{
			ServerCommand cmd;
			if ((cmd = this.commandlist.get(lower_prefix)) != null)
			{
				return cmd;
			}
		}
		throw new NullPointerException("The Command (" + lower_prefix + ") does not exist!");
	}

	@SuppressWarnings("incomplete-switch")
	public final boolean performServer(GuildEntity guildentity, SlashCommandInteractionEvent event) 
	{
		final ServerCommand cmd;
		final String prefix = event.getName();
		
		final Member member = event.getMember();
		final TextChannel channel = event.getTextChannel();
		final Guild guild = event.getGuild();

		try 
		{
			cmd = getCommand(prefix);
		} 
		catch (NullPointerException e)
		{
			return false;
		}
		
		if (cmd != null) 
		{
			switch (cmd.getMainPermission()) 
			{
				case DEVELOPER:
					if (Settings.DEVELOPER_ID_LIST.contains(member.getIdLong())) 
					{
						cmd.performMainCMD(member, channel, guild, guildentity, event);
						return true;
					}
					break;
	
				case ADMIN:
					if (member.hasPermission(Permission.MANAGE_SERVER) || member.hasPermission(Permission.ADMINISTRATOR)) 
					{
						cmd.performMainCMD(member, channel, guild, guildentity, event);
					}
					else 
					{
						Messenger.sendErrorMessage(event, "user-no-permmisions", "MANAGE_SERVER");
					}
					return true;
				
				case EVERYONE:
					cmd.performMainCMD(member, channel, guild, guildentity, event);
					return true;
			}
		}
		return false;
	}
}

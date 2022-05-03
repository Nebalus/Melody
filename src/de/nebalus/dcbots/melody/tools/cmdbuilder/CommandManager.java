package de.nebalus.dcbots.melody.tools.cmdbuilder;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.nebalus.dcbots.melody.commands.TestCommand;
import de.nebalus.dcbots.melody.commands.info.HelpCommand;
import de.nebalus.dcbots.melody.commands.info.InviteCommand;
import de.nebalus.dcbots.melody.commands.info.PingCommand;
import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.tools.ConsoleLogger;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.Messenger;
import de.nebalus.dcbots.melody.tools.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public final class CommandManager {

	private final ConcurrentHashMap<String, ServerCommand> command;

	public CommandManager(Melody melody) {
		this.command = new ConcurrentHashMap<String, ServerCommand>();

		registerCommands(new TestCommand(), new InviteCommand(), new PingCommand(), new HelpCommand());
	}

	private final void registerCommands(ServerCommand... cmd) {

		final ArrayList<CommandData> slashcommands = new ArrayList<CommandData>();

		for (ServerCommand sc : cmd) {

			ConsoleLogger.debug("CMD-BUILDER", "Loading CMD... " + "HASH: " + sc.hashCode() + " | " + "PREFIX: " + sc.getPrefix());
		
			command.put(sc.getPrefix().toLowerCase(), sc);

			// Muss noch bearbeitet werden
			if (sc.getSubCommands().isEmpty()) {
				slashcommands.add(Commands.slash(sc.getPrefix(), sc.getDescription()));
			}
			
		}

		if (Melody.getConfig()._allowslashcommands) {
			for (JDA jda : Melody.getShardManager().getShards()) {
				CommandListUpdateAction slashupdater = jda.updateCommands();
				slashcommands.forEach((commanddata) -> {
					slashupdater.addCommands(commanddata);
				});
				slashupdater.queue();
				ConsoleLogger.debug("SLASH-BUILDER", "EXPORT generated Slash commands to Shard [ID:" + jda.getShardInfo().getShardId() + "]");
			}
		}
		/*
		 * for(ServerCommand sc : cmd) { if(!sc.getType().equals(CommandType.CHAT)) {
		 * for(JDA jda : melody.shardMan.getShards()) { CommandListUpdateAction
		 * slashcommands = jda.updateCommands();
		 * if(Melody.INSTANCE._config._allowslashcommands) { for(Command sc : cmd) {
		 * if(sc.getCommandType().isSlash()) { if(sc.getCommandOptions() == null) {
		 * slashcommands.addCommands(new CommandData(sc.getCommandPrefix()[0],
		 * sc.getCommandDescription())); ConsoleLogger.debug("SLASH-BUILDER",
		 * sc.getCommandPrefix()[0]+" added naked Slash Command");
		 * commands.put(sc.getCommandPrefix()[0], sc); }else {
		 * slashcommands.addCommands(new CommandData(sc.getCommandPrefix()[0],
		 * sc.getCommandDescription()).addOptions(sc.getCommandOptions()));
		 * ConsoleLogger.debug("SLASH-BUILDER",
		 * sc.getCommandPrefix()[0]+" added Slash Command");
		 * commands.put(sc.getCommandPrefix()[0], sc); } } } } /* //Init Help command at
		 * last ServerCommand helpcommand = new HelpCommand(rawcommands);
		 * slashcommands.addCommands(new CommandData(helpcommand.getCommandPrefix()[0],
		 * helpcommand.getCommandDescription()).addOptions(helpcommand.getCommandOptions
		 * ())); ConsoleLogger.debug("SLASH-BUILDER",
		 * helpcommand.getCommandPrefix()[0]+" added Slash Command");
		 * this.slashcommands.put(helpcommand.getCommandPrefix()[0], helpcommand);
		 * 
		 * slashcommands.queue(); } } }
		 */
	}

	public final ArrayList<ServerCommand> getCommands() {
		final ArrayList<ServerCommand> rawcmd = new ArrayList<ServerCommand>();
		for (Map.Entry<String, ServerCommand> entry : command.entrySet()) {
			rawcmd.add(entry.getValue());
		}
		return rawcmd;
	}

	public final ServerCommand getCommand(String prefix) throws NullPointerException {
		final String lower_prefix = prefix.toLowerCase();
		if (this.command.containsKey(lower_prefix)) {
			ServerCommand cmd;
			if ((cmd = this.command.get(lower_prefix)) != null) {
				return cmd;
			}
		}
		throw new NullPointerException();
	}

	@SuppressWarnings("incomplete-switch")
	public final boolean performServer(GuildEntity guildentity, SlashCommandInteractionEvent event) {
		ServerCommand cmd;
		final String prefix = event.getName();
		
		final Member member = event.getMember();
		final TextChannel channel = event.getTextChannel();
		final Guild guild = event.getGuild();

		try {
			cmd = getCommand(prefix);
		} catch (NullPointerException e) {
			return false;
		}
		if (cmd != null) {
			switch (cmd.getMainPermission()) {
				case DEVELOPER:
					if (Settings.DEVELOPER_ID_LIST.contains(member.getIdLong())) {
						cmd.performMainCMD(member, channel, guild, guildentity, event);
						return true;
					}
					break;
	
				case ADMIN:
					if (member.hasPermission(Permission.MANAGE_SERVER) || member.hasPermission(Permission.ADMINISTRATOR)) {
						cmd.performMainCMD(member, channel, guild, guildentity, event);
					} else {
						Messenger.sendErrorMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "user-no-permmisions", "MANAGE_SERVER"), true);
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

package old.de.nebalus.dcbots.melody.tools.cmdbuilder;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import old.de.nebalus.dcbots.melody.core.constants.Melody;
import old.de.nebalus.dcbots.melody.core.constants.Settings;
import old.de.nebalus.dcbots.melody.tools.ConsoleLogger;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import old.de.nebalus.dcbots.melody.tools.messenger.Messenger;

public final class CommandManager {

	private final ConcurrentHashMap<String, SlashCommand> commandlist;

	public CommandManager() {
		commandlist = new ConcurrentHashMap<>();
	}

	public final void registerCommands(SlashCommand... cmd) {
		for (SlashCommand sc : cmd) {

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

		// Exports all registered Slashcommands to the Discord Shards
		for (JDA jda : Melody.getShardManager().getShards()) {
			CommandListUpdateAction slashupdater = jda.updateCommands();
			for (SlashCommand sc : cmd) {
				slashupdater.addCommands(sc.getSlashCommandData());
			}
			slashupdater.queue();
			ConsoleLogger.debug("SLASH-BUILDER",
					"EXPORT generated Slash commands to Shard [ID:" + jda.getShardInfo().getShardId() + "]");
		}
	}

	public final ArrayList<SlashCommand> getCommands() {
		final ArrayList<SlashCommand> rawcmd = new ArrayList<>();

		for (Map.Entry<String, SlashCommand> entry : commandlist.entrySet()) {
			rawcmd.add(entry.getValue());
		}
		return rawcmd;
	}

	public final SlashCommand getCommand(String prefix) throws NullPointerException {
		final String lower_prefix = prefix.toLowerCase();
		if (commandlist.containsKey(lower_prefix)) {
			SlashCommand cmd;
			if ((cmd = commandlist.get(lower_prefix)) != null) {
				return cmd;
			}
		}
		throw new NullPointerException("The Command (" + lower_prefix + ") does not exist!");
	}

	public final boolean performSlashGuild(SlashInteractionExecuter executer, PermissionGroup permgroup,
			GuildEntity guildentity, SlashCommandInteractionEvent event) {
		final Member member = event.getMember();
		final MessageChannelUnion channel = event.getChannel();
		final Guild guild = event.getGuild();

		switch (permgroup) {
		case DEVELOPER:
			if (Settings.DEVELOPER_ID_LIST.contains(member.getIdLong())) {
				executer.executeGuild(member, channel, guild, guildentity, event);
				return true;
			}
			break;

		case ADMIN:
			if (member.hasPermission(Permission.MANAGE_SERVER) || member.hasPermission(Permission.ADMINISTRATOR)) {
				executer.executeGuild(member, channel, guild, guildentity, event);
			} else {
				Messenger.sendErrorMessage(event, "user-no-permmisions", "MANAGE_SERVER");
			}
			return true;

		case EVERYONE:
			executer.executeGuild(member, channel, guild, guildentity, event);
			return true;

		default:
			// Error Message Senden das keine PermissionGroup gefunden wurde
			break;

		}
		return false;
	}
}

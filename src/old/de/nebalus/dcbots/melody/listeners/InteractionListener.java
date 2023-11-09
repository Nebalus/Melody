package old.de.nebalus.dcbots.melody.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import old.de.nebalus.dcbots.melody.core.constants.Melody;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.CommandManager;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import old.de.nebalus.dcbots.melody.tools.messenger.Messenger;

public final class InteractionListener extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		final CommandManager commandmanager = Melody.getCommandManager();
		final String cmdprefix = event.getName().toLowerCase();
		final String cmdpath = event.getCommandPath();
		final String[] subcmdrelations = cmdpath.split("/");
		final SlashCommand cmd;
		final SlashInteractionExecuter executer;

		try {
			cmd = commandmanager.getCommand(cmdprefix);
		} catch (NullPointerException e) {
			e.printStackTrace();
			event.reply("This command is currently not available.").setEphemeral(true).queue();
			return;
		}

		switch (subcmdrelations.length) {
		case 1:
			executer = cmd.getExecuter();
			break;

		case 2:
		case 3:
			executer = cmd.getSubCommandByPath(cmdpath.substring(cmdprefix.length() + 1)).getExecuter();
			break;

		default:
			event.reply("The Executable from the CMDPATH:" + cmdpath + " could not be found!!!").setEphemeral(true).queue();
			return;
		}

		if (event.isFromGuild()) {
			Guild guild = event.getGuild();
			GuildEntity ge = Melody.getGuildEntity(guild);
			if (!ge.isRateLimited()) {
				ge.addRateRequest();
				try {
					if (!commandmanager.performSlashGuild(executer, cmd.getPermissionGroup(), ge, event)) {
						event.reply("This command is currently not available.").setEphemeral(true).queue();
					}
				} catch (InsufficientPermissionException e) {
					Messenger.sendErrorMessage(event, "bot-no-permmisions", e.getPermission());
				}
			} else if (!ge.ratelimitmsgsend) {
				ge.ratelimitmsgsend = true;
				Messenger.sendErrorMessage(event, "ratelimit", Melody.getConfig().RATELIMITREQUEST, Melody.getConfig().RATELIMITITERATIONDURATION);
			}
		} else if (!cmd.isGuildOnly()) {

		} else {
			event.reply("Hmm Something was not setup correctly!!!").setEphemeral(true).queue();
		}
	}

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {

	}

	@Override
	public void onModalInteraction(ModalInteractionEvent event) {

	}

}
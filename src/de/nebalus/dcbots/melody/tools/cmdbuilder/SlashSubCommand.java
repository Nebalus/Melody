package de.nebalus.dcbots.melody.tools.cmdbuilder;

import javax.annotation.Nonnull;

import de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SlashSubCommand {
	private final SubcommandData subcommanddata;
	private SlashInteractionExecuter executer;

	public SlashSubCommand(String name, String description) {
		subcommanddata = new SubcommandData(name, description);
	}

	@Nonnull
	public SlashSubCommand addOption(OptionData optiondata) {
		subcommanddata.addOptions(optiondata);
		return this;
	}

	public SubcommandData getSubCommandData() {
		return subcommanddata;
	}

	@Nonnull
	public SlashSubCommand setExecuter(SlashInteractionExecuter executer) {
		this.executer = executer;
		return this;
	}

	public SlashInteractionExecuter getExecuter() {
		return executer;
	}
}

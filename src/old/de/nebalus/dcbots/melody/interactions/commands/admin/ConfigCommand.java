package old.de.nebalus.dcbots.melody.interactions.commands.admin;

import java.util.ArrayList;

import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import old.de.nebalus.dcbots.melody.tools.messenger.Language;

public class ConfigCommand extends SlashCommand {

	public ConfigCommand() {
		super("config");
		setPermissionGroup(PermissionGroup.ADMIN);
		setDescription(getDescription());

		ArrayList<Choice> languagechoices = new ArrayList<>();
		for (Language lang : Language.values()) {
			languagechoices.add(new Choice(lang.name(), lang.getCode()));
		}
		addOption(new OptionData(OptionType.STRING, "language", "Enter a new language")
				.setRequired(false)
				.addChoices(languagechoices));
	}
}

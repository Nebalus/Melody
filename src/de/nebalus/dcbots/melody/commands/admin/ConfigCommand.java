package de.nebalus.dcbots.melody.commands.admin;

import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.messenger.Language;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ConfigCommand extends SlashCommand
{
	
	public ConfigCommand() 
	{
		super("config");
		setPermissionGroup(PermissionGroup.ADMIN);
		setDescription(getDescription());
		
		addOption(new OptionData(OptionType.STRING, "language", "Enter a new language")
			.setRequired(false)
			.addChoice(Language.ENGLISH.name(), Language.ENGLISH.getCode())
			.addChoice(Language.GERMAN.name(), Language.GERMAN.getCode()));
	}
}

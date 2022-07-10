package de.nebalus.dcbots.melody.commands.admin;

import de.nebalus.dcbots.melody.tools.cmdbuilder.InternPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.messenger.Language;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ConfigCommand extends ServerCommand
{
	
	public ConfigCommand() 
	{
		super("config");
		setInternPermission(InternPermission.ADMIN);
		setDescription(getDescription());
		setSlashCommandData(
				Commands.slash(getPrefix(), getDescription())	
					.addOptions(new OptionData(OptionType.STRING, "language", "Enter a new language", false)
						.addChoice(Language.ENGLISH.name(), Language.ENGLISH.getCode())
						.addChoice(Language.GERMAN.name(), Language.GERMAN.getCode())
					)
				);
	}
}

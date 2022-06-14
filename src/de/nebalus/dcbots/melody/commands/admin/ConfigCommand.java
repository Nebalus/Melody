package de.nebalus.dcbots.melody.commands.admin;

import de.nebalus.dcbots.melody.tools.cmdbuilder.CommandPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;

public class ConfigCommand extends ServerCommand{
	
	public ConfigCommand() {
		super("config");
		setMainPermission(CommandPermission.ADMIN);
	}
}

package de.nebalus.dcbots.melody.commands.music;

import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;

public class StopCommand extends ServerCommand
{

	public StopCommand()
	{
		super("stop");
		setDescription("Stops the player and clear the queue.");
	}

}

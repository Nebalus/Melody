package de.nebalus.dcbots.melody.commands.music;

import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;

public class StopCommand extends SlashCommand
{

	public StopCommand()
	{
		super("stop");
		setDescription("Stops the player and clear the queue.");
	}

}

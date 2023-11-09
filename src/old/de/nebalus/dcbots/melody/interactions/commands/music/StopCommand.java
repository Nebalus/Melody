package old.de.nebalus.dcbots.melody.interactions.commands.music;

import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;

public class StopCommand extends SlashCommand {

	public StopCommand() {
		super("stop");
		setDescription("Stops the player and clear the queue.");
	}

}

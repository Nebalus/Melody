package de.nebalus.dcbots.melody.commands.music;

import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PlayCommand extends SlashCommand
{

	public PlayCommand() 
	{
		super("play");
		setDescription("Plays a song.");
		addOption(new OptionData(OptionType.STRING, "query", "Please enter a (song name/url)")
			.setRequired(true));
	}
}

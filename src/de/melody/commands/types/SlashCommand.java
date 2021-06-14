package de.melody.commands.types;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface SlashCommand {
	
	public CommandData getCommandData();
	
	public void performSlashCommand(SlashCommandEvent slash);
	
}

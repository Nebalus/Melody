 package de.nebalus.dcbots.melody.tools.cmdbuilder;

import java.util.ArrayList;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public class SlashSubCommandGroup extends SubcommandGroupData
{
	
	private final ArrayList<SlashSubCommand> subcommands;
	
	public SlashSubCommandGroup(String name, String description) 
	{
		super(name, description);
		subcommands = new ArrayList<SlashSubCommand>();
	}
	
	public SlashSubCommandGroup addSubCommand(SlashSubCommand subcommand)
	{
		subcommands.add(subcommand);
		addSubcommands(subcommand.getSubCommandData());
		return this;
	}
	
	public ArrayList<SlashSubCommand> getServerSubCommands()
	{
		return subcommands;
	}

}

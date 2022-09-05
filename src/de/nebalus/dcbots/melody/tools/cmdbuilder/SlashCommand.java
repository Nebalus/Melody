package de.nebalus.dcbots.melody.tools.cmdbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import de.nebalus.dcbots.melody.tools.ConsoleLogger;
import de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SlashCommand
{
	
	private PermissionGroup permissiongroup;	
	private SlashCommandData slashcommanddata;
	private HashMap<String, SlashSubCommand> subcommands;
	private SlashInteractionExecuter executer;
	
	protected SlashCommand(String prefix) 
	{
		ConsoleLogger.debug("CMD-BUILDER", "Loading CMD \"" + prefix.toLowerCase() + "\"");
		
		subcommands = new HashMap<String, SlashSubCommand>();
		permissiongroup = PermissionGroup.DEVELOPER;
		slashcommanddata = Commands.slash(prefix.toLowerCase(), "What will be here is written in the stars :P");
		slashcommanddata.setGuildOnly(true);
		executer = null;
	}
	
	@Nonnull
	protected void setGuildOnly(boolean isguildonly)
	{
		slashcommanddata.setGuildOnly(isguildonly);
	}
	
	public boolean isGuildOnly()
	{
		return slashcommanddata.isGuildOnly();
	}
	
	@Nonnull
	protected void setExecuter(SlashInteractionExecuter executer)
	{
		this.executer = executer;
	}
	
	public SlashInteractionExecuter getExecuter()
	{
		return executer;
	}
	
	//Gets the Command description
	public String getDescription() 
	{
		return slashcommanddata.getDescription();
	}
	
	@Nonnull
	protected void setDescription(String description) 
	{
		slashcommanddata.setDescription(description);
	}
	
	public PermissionGroup getPermissionGroup() 
	{
		return permissiongroup;
	}
	
	@Nonnull
	protected void setPermissionGroup(PermissionGroup permissiongroup) 
	{
		this.permissiongroup = permissiongroup;
	}
	 
	public String getPrefix()
	{
		return slashcommanddata.getName();
	}
	
	@Nonnull
	protected void setPrefix(String prefix) 
	{
		slashcommanddata.setName(prefix.toLowerCase());
	}
	
	public SlashCommandData getSlashCommandData()
	{	
		return slashcommanddata;
	}
	
	public ArrayList<SlashSubCommand> getSubCommands()
	{
		final ArrayList<SlashSubCommand> subcmd = new ArrayList<SlashSubCommand>();
		
		for (Map.Entry<String, SlashSubCommand> entry : subcommands.entrySet()) 
		{
			subcmd.add(entry.getValue());
		}
		return subcmd;
	}
	
	@Nonnull
	protected void addSubCommand(SlashSubCommand subcommand) 
	{	
		final SubcommandData subcommanddata = subcommand.getSubCommandData();
		
		ConsoleLogger.debug("CMD-BUILDER", "Loading SUBCMD | PATH: " + getPrefix() + "/" + subcommanddata.getName() + " | NUMOPTIONSPROVIDED: " + subcommanddata.getOptions().size());
		
		subcommands.put(subcommanddata.getName(), subcommand);
		slashcommanddata.addSubcommands(subcommanddata);
	}
	
	public SlashSubCommand getSubCommandByPath(String path)
	{
		if(subcommands.containsKey(path))
		{
			return subcommands.get(path);
		}
		throw new NullPointerException("The SubCommand (" + getPrefix() + "/" + path + ") does not exist!");
	}
	
	@Nonnull
	protected void addSubCommandGroup(SlashSubCommandGroup subcommandgroup) 
	{
		for(SlashSubCommand ssc : subcommandgroup.getServerSubCommands())
		{
			final String path = subcommandgroup.getName() + "/" + ssc.getSubCommandData().getName();
			
			ConsoleLogger.debug("CMD-BUILDER", "Loading SUBCMD | PATH: " + getPrefix() + "/" + path + " | NUMOPTIONSPROVIDED: " + ssc.getSubCommandData().getOptions().size());
			
			subcommands.put(path, ssc);
		}
		
		slashcommanddata.addSubcommandGroups(subcommandgroup);
	}
	
	@Nonnull
	protected void addOption(OptionData optiondata)
	{
		slashcommanddata.addOptions(optiondata);
	}
	
	@Nonnull
	protected void setDefaultPermissions(DefaultMemberPermissions permission)
	{
		slashcommanddata.setDefaultPermissions(permission);
	}

}

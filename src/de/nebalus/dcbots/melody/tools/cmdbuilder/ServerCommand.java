package de.nebalus.dcbots.melody.tools.cmdbuilder;

import java.util.ArrayList;

import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class ServerCommand 
{
	
	private String description;
	private CommandPermission mainpermission;
	private String prefix;
	
	private SlashCommandData slashcommanddata;
	private ArrayList<SubCommand> subcommands;
	
	protected ServerCommand(String prefix) 
	{
		this.subcommands = new ArrayList<SubCommand>();
		this.description = "What will be here is written in the stars :P";
		this.mainpermission = CommandPermission.DEVELOPER;
		this.prefix = prefix.toLowerCase();
		this.slashcommanddata = Commands.slash(prefix, description);
	}
	
	public String getDescription() 
	{
		return description;
	}
	
	protected void setDescription(String description) 
	{
		this.description = description;
	}
	
	public CommandPermission getMainPermission() 
	{
		return this.mainpermission;
	}
	
	protected void setMainPermission(CommandPermission mainpermission) 
	{
		this.mainpermission = mainpermission;
	}
	 
	public String getPrefix()
	{
		return this.prefix;
	}
	
	protected void setPrefix(String prefix) 
	{
		this.prefix = prefix.toLowerCase();
	}
	
	public SlashCommandData getSlashCommandData()
	{
		slashcommanddata.setDescription(this.description);
		slashcommanddata.setName(this.prefix);
		
		return this.slashcommanddata;
	}
	
	protected void setSlashCommandData(SlashCommandData slashcommanddata) 
	{
		this.slashcommanddata = slashcommanddata;
	}
	
	public ArrayList<SubCommand> getSubCommands()
	{
		return this.subcommands;
	}
	
	protected void addSubCommand(SubCommand subcommand) 
	{
		this.subcommands.add(subcommand);
	}
	
	public void performMainCmd(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) 
	{
		
	}
	
	
	/*
	protected void finish() {
		
	}
	*/

}

package de.nebalus.dcbots.melody.tools.cmdbuilder;

import java.util.ArrayList;

import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ServerCommand {
	
	private String description;
	private CommandPermission mainpermission;
	private String prefix;
	
	private ArrayList<SubCommand> subcommands;
	
	protected ServerCommand(String prefix) {
		this.subcommands = new ArrayList<SubCommand>();
		this.description = "What will be here is written in the stars :P";
		this.mainpermission = CommandPermission.DEVELOPER;
		this.prefix = prefix.toLowerCase();
	}
	
	public String getDescription() {
		return description;
	}
	
	protected void setDescription(String description) {
		this.description = description;
	}
	
	public CommandPermission getMainPermission() {
		return this.mainpermission;
	}
	
	protected void setMainPermission(CommandPermission mainpermission) {
		this.mainpermission = mainpermission;
	}
	 
	public String getPrefix(){
		return this.prefix;
	}
	
	protected void setPrefix(String prefix) {
		this.prefix = prefix.toLowerCase();
	}
	
	public ArrayList<SubCommand> getSubCommands(){
		return this.subcommands;
	}
	
	protected void addSubCommand(SubCommand subcommand) {
		this.subcommands.add(subcommand);
	}
	
	public void performMainCMD(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) {
		
	}
	
	
	/*
	protected void finish() {
		
	}
	*/

}

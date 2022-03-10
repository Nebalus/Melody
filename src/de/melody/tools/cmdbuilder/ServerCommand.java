package de.melody.tools.cmdbuilder;

import java.util.ArrayList;

import de.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ServerCommand {
	private CommandType type;
	private String description;
	private CommandPermission mainpermission;
	private String[] prefixs;
	
	private ArrayList<SubCommand> subcommands;
	
	protected ServerCommand() {
		this.subcommands = new ArrayList<SubCommand>();
		this.type = CommandType.NULL;
		this.description = "What will be here is written in the stars :P";
		this.mainpermission = CommandPermission.DEVELOPER;
		this.prefixs = new String[] {};
	}
	
	public CommandType getType() {
		return this.type;
	}
	
	protected void setType(CommandType type) {
		this.type = type;
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
	 
	public String[] getPrefixs(){
		return this.prefixs;
	}
	
	protected void setPrefixes(String... prefixs) {
		this.prefixs = prefixs;
	}
	
	public ArrayList<SubCommand> getSubCommands(){
		return this.subcommands;
	}
	
	protected void addSubCommand(SubCommand subcommand) {
		this.subcommands.add(subcommand);
	}
	
	public void performMainCMD(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		
	}
	
	public void performMainSlashCMD(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		
	}
	
	
	/*
	protected void finish() {
		
	}
	*/

}

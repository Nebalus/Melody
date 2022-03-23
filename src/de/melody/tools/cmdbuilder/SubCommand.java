package de.melody.tools.cmdbuilder;

import de.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SubCommand {
	
	private String description;
	private final String prefix;
	private final CommandPermission permission;
	private final CommandType type;
	
	public SubCommand(String prefix, CommandPermission permission, CommandType type) {
		this.description = "What will be here is written in the stars :P";
		this.permission = permission;
		this.prefix = prefix;
		this.type = type;
	}
	
	public void execute(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		
	}
	
	public CommandType getType() {
		return type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public CommandPermission getPermission() {
		return permission;
	}
	
	protected void setDescription(String description) {
		this.description = description;
	}
	
}

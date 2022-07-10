package de.nebalus.dcbots.melody.tools.cmdbuilder;

import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SubCommand {
	
	private final String description;
	private final String[] suffix;
	private final InternPermission permission;
	
	public SubCommand(String prefix, InternPermission permission) {
		this.description = "What will be here is written in the stars :P";
		this.permission = permission;
		this.suffix = prefix.split(" ");
	}
	
	public SubCommand(String prefix, InternPermission permission, String discription) {
		this.description = discription;
		this.permission = permission;
		this.suffix = prefix.split(" ");
	}
	
	public void execute(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		
	}
	
	public String getDescription() {
		return description;
	}
	
	public String[] getSuffix() {
		return suffix;
	}
	
	public InternPermission getPermission() {
		return permission;
	}
}

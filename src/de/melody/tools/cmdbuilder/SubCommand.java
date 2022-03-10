package de.melody.tools.cmdbuilder;

import de.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SubCommand {
	
	private final String description;
	private final String prefix;
	private final CommandPermission permission;
	
	public SubCommand(String prefix, CommandPermission permission) {
		this.description = "What will be here is written in the stars :P";
		this.permission = permission;
		this.prefix = prefix;
	}
	
	public void execute(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		
	}
	
}

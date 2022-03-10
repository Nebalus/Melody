package de.melody.tools.cmdbuilder;

import java.util.concurrent.ConcurrentHashMap;

import de.melody.commands.TestCommand;
import de.melody.tools.ConsoleLogger;
import de.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public final class CommandManager {
	
	private ConcurrentHashMap<Integer, ServerCommand> command;
	private ConcurrentHashMap<String, Integer> commandhash;
	
	public CommandManager() {
		this.command = new ConcurrentHashMap<Integer, ServerCommand>();
		this.commandhash = new ConcurrentHashMap<String, Integer>();
		
		registerCommands(new TestCommand());
	}
	
	private void registerCommands(ServerCommand... cmd) {
		for(ServerCommand sc : cmd) {
			final int hashcode = sc.hashCode();
			
			ConsoleLogger.debug("CMD-BUILDER", 
					"Loading CMD... "
					+ "HASH: " + hashcode + " / "
					+ "PREFIX: " + sc.getPrefixs()[0] + " / "
					+ "TYPE: " + sc.getType().name());
			
			command.put(hashcode, sc);
			
			for(String prefix : sc.getPrefixs()) {
				commandhash.put(prefix.toLowerCase(), hashcode);
			}
		}
	}
	
	public boolean performServer(String command, Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {	
		ServerCommand cmd;
		ConsoleLogger.info(this.commandhash.get(command.toLowerCase()));
		if(this.commandhash.containsKey(command.toLowerCase())) {
			if((cmd = this.command.get(this.commandhash.get(command.toLowerCase()))) != null && cmd.getType().isChat()) {
				cmd.performMainCMD(member, channel, message, guild, guildentity);
				return true;
			}
		}
		return false;
	}
}

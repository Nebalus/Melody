package de.melody.tools.cmdbuilder;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import de.melody.commands.TestCommand;
import de.melody.commands.info.HelpCommand;
import de.melody.commands.info.InfoCommand;
import de.melody.commands.info.InviteCommand;
import de.melody.commands.info.PingCommand;
import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.tools.ConsoleLogger;
import de.melody.tools.entitymanager.entitys.GuildEntity;
import de.melody.tools.messenger.Messenger;
import de.melody.tools.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public final class CommandManager {
	
	private final ConcurrentHashMap<Integer, ServerCommand> command;
	private final ConcurrentHashMap<String, Integer> commandhash;

	
	public CommandManager(Melody melody) {
		this.command = new ConcurrentHashMap<Integer, ServerCommand>();
		this.commandhash = new ConcurrentHashMap<String, Integer>();
		
		registerCommands(new TestCommand(), new HelpCommand(), new InviteCommand(), new PingCommand(), new InfoCommand());
	}
	
	private void registerCommands(ServerCommand... cmd) {
		
		final ArrayList<CommandData> slashcommands = new ArrayList<CommandData>();
		
		for(ServerCommand sc : cmd) {	
			
			ConsoleLogger.debug("CMD-BUILDER", 
					"Loading CMD... "
					+ "HASH: " + sc.hashCode() + " | "
					+ "PREFIX: " + sc.getPrefixs()[0] + " | "
					+ "TYPE: " + sc.getType().name());
			
			if(!sc.getType().equals(CommandType.NULL)) {
				
				command.put(sc.hashCode(), sc);
			
				for(String prefix : sc.getPrefixs()) {
					commandhash.put(prefix.toLowerCase(), sc.hashCode());
				}
				
				if(sc.getType().isSlash()) {
					//Muss noch bearbeitet werden
					if(sc.getSubCommands().isEmpty()) {
						slashcommands.add(new CommandData(sc.getPrefixs()[0], sc.getDescription()));
					}
				}
			}else {
				ConsoleLogger.error("CMD-BUILDER", "Error Loading CMD PREFIX: " + sc.getPrefixs()[0] + " because the TYPE is NULL");
			}
		}
		
		if(Melody.getConfig()._allowslashcommands) {
			for(JDA jda : Melody.getShardManager().getShards()) {
				CommandListUpdateAction slashupdater = jda.updateCommands();
				slashcommands.forEach((commanddata) ->{
					slashupdater.addCommands(commanddata);
				});
				slashupdater.queue();
				ConsoleLogger.debug("SLASH-BUILDER", "EXPORT generated Slash commands to Shard [ID:"+jda.getShardInfo().getShardId()+"]");
			}
		}
		/*
		for(ServerCommand sc : cmd) {	
			if(!sc.getType().equals(CommandType.CHAT)) {
				for(JDA jda : melody.shardMan.getShards()) {
					CommandListUpdateAction slashcommands = jda.updateCommands();
					if(Melody.INSTANCE._config._allowslashcommands) {
						for(Command sc : cmd) {
							if(sc.getCommandType().isSlash()) {
								if(sc.getCommandOptions() == null) {
									slashcommands.addCommands(new CommandData(sc.getCommandPrefix()[0], sc.getCommandDescription()));
									ConsoleLogger.debug("SLASH-BUILDER", sc.getCommandPrefix()[0]+" added naked Slash Command");
									commands.put(sc.getCommandPrefix()[0], sc);
								}else {
									slashcommands.addCommands(new CommandData(sc.getCommandPrefix()[0], sc.getCommandDescription()).addOptions(sc.getCommandOptions()));
									ConsoleLogger.debug("SLASH-BUILDER", sc.getCommandPrefix()[0]+" added Slash Command");
									commands.put(sc.getCommandPrefix()[0], sc);
								}
							}
						}
					}
					/*
					//Init Help command at last
					ServerCommand helpcommand = new HelpCommand(rawcommands);
					slashcommands.addCommands(new CommandData(helpcommand.getCommandPrefix()[0], helpcommand.getCommandDescription()).addOptions(helpcommand.getCommandOptions()));
					ConsoleLogger.debug("SLASH-BUILDER", helpcommand.getCommandPrefix()[0]+" added Slash Command");
					this.slashcommands.put(helpcommand.getCommandPrefix()[0], helpcommand);
					 
					slashcommands.queue();
				}
			}
		}
		*/
	}
	
	public ArrayList<ServerCommand> getCommands(){
		ArrayList<ServerCommand> rawcmd = new ArrayList<ServerCommand>();	
		for(Entry<Integer, ServerCommand> entry : command.entrySet()) {
			rawcmd.add(entry.getValue());
		}
		return rawcmd;
	}
	
	public boolean performServer(String[] command, Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {	
		ServerCommand cmd;
		String prefix = command[0].toLowerCase();
		
		ConsoleLogger.info(this.commandhash.get(prefix));
		if(this.commandhash.containsKey(prefix)) {
			if((cmd = this.command.get(this.commandhash.get(prefix))) != null && cmd.getType().isChat()) {
				switch(cmd.getMainPermission()) {
				
				case DEVELOPER:
					if(Constants.DEVELOPERIDS.contains(member.getIdLong())) {
						cmd.performMainCMD(member, channel, message, guild, guildentity);
						return true;
					}
					break;
					
				case ADMIN:
					if(member.hasPermission(Permission.MANAGE_SERVER) || member.hasPermission(Permission.ADMINISTRATOR)) {
						cmd.performMainCMD(member, channel, message, guild, guildentity);
					}else {
						Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "user-no-permmisions", "MANAGE_SERVER"));
					}
					return true;
					
				default:
					cmd.performMainCMD(member, channel, message, guild, guildentity);
					return true;
				}
			}
		}
		return false;
	}
}

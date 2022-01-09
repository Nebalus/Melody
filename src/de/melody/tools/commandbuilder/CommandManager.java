package de.melody.tools.commandbuilder;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.tools.ConsoleLogger;
import de.melody.tools.messenger.Messenger;
import de.melody.tools.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class CommandManager {

	private ConcurrentHashMap<String, Command> commands;
	
	private ArrayList<Command> rawcommands;
	
	private Melody melody;
	public CommandManager(Melody melody) {
		this.commands = new ConcurrentHashMap<>();
		this.melody = melody;
		this.rawcommands = new ArrayList<Command>();
	}
	
	public ArrayList<Command> getRawCommands(){
		return rawcommands;
	}
	
	//Versuche die beiden forschleifen zusammenfügen
	public CommandManager registerCommands(Command... cmd) {
		for(Command sc : cmd) {
			rawcommands.add(sc);
			if(sc.getCommandType().isChat()) {
				for(String command : sc.getCommandPrefix()) {
					ConsoleLogger.debug("COMMAND-REGISTER", "Loading Chat Command '"+command+"'");
					this.commands.put(command, sc);
				}	
			}
		}
		for(JDA jda : melody._shardMan.getShards()) {
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
			 */
			slashcommands.queue();
		}
		return this;
	}
	
	public boolean performServer(String command, Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {	
		Command cmd;
		if((cmd = this.commands.get(command.toLowerCase())) != null && cmd.getCommandType().isChat()) {
			switch(cmd.getMainPermmision()) {
				case DEVELOPER:
					if(Constants.DEVELOPERIDS.contains(member.getIdLong())) {
						cmd.performCommand(member, channel, message, guild, guildentity);
						return true;
					}
					break;
				case ADMIN:
					if(member.hasPermission(Permission.MANAGE_SERVER) || member.hasPermission(Permission.ADMINISTRATOR)) {
						cmd.performCommand(member, channel, message, guild, guildentity);
					}else {
						Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "user-no-permmisions", "MANAGE_SERVER"));
					}
					return true;
				case DJ:
					if(guildentity.isMemberDJ(member)) {
						cmd.performCommand(member, channel, message, guild, guildentity);
					}else {
						Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.user-not-dj"));
					}
					return true;
				default:
					cmd.performCommand(member, channel, message, guild, guildentity);
					return true;
			}
		}
		return false;
	}
	
	public boolean performServerSlash(String name, Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		Command cmd;
		if((cmd = this.commands.get(name.toLowerCase())) != null && cmd.getCommandType().isSlash()) {
			switch(cmd.getMainPermmision()) {
				case DEVELOPER:
					if(Constants.DEVELOPERIDS.contains(member.getIdLong())) {
						cmd.performSlashCommand(member, channel, guild, guildentity, event);
						return true;
					}
					break;
				case ADMIN:
					if(member.hasPermission(Permission.MANAGE_SERVER) || member.hasPermission(Permission.ADMINISTRATOR)) {
						cmd.performSlashCommand(member, channel, guild, guildentity, event);
					}else {
						Messenger.sendErrorSlashMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "user-no-permmisions", "MANAGE_SERVER"));
					}
					return true;
				case DJ:
					if(guildentity.isMemberDJ(member)) {
						cmd.performSlashCommand(member, channel, guild, guildentity, event);
					}else {
						Messenger.sendErrorSlashMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "music.user-not-dj"));
					}
					return true;
				default:
					cmd.performSlashCommand(member, channel, guild, guildentity, event);
					return true;
			}
		}
		return false;
	}
}

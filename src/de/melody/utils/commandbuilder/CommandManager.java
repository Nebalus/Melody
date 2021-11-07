package de.melody.utils.commandbuilder;

import java.util.concurrent.ConcurrentHashMap;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.utils.Utils.ConsoleLogger;
import de.melody.utils.messenger.Messenger;
import de.melody.utils.messenger.Messenger.ErrorMessageBuilder;
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

	private ConcurrentHashMap<String, ServerCommand> chatcommands;
	private ConcurrentHashMap<String, ServerCommand> slashcommands;
	
	private Melody melody;
	public CommandManager(Melody melody) {
		this.chatcommands = new ConcurrentHashMap<>();
		this.melody = melody;
		this.slashcommands = new ConcurrentHashMap<>();
	}
	
	public CommandManager registerCommands(ServerCommand... cmd) {
		for(ServerCommand sc : cmd) {
			if(sc.getCommandType().isChat()) {
				for(String command : sc.getCommandPrefix()) {
					ConsoleLogger.debug("COMMAND-REGISTER", "Loading Command '"+command+"'");
					this.chatcommands.put(command, sc);
				}	
			}
		}
		for(JDA jda : melody.shardMan.getShards()) {
			CommandListUpdateAction slashcommands = jda.updateCommands();
			for(ServerCommand sc : cmd) {
				if(sc.getCommandType().isSlash()) {
					if(sc.getCommandOptions() == null) {
						slashcommands.addCommands(new CommandData(sc.getCommandPrefix().get(0), sc.getCommandDescription()));
						ConsoleLogger.debug("SLASH-BUILDER", sc.getCommandPrefix().get(0)+" added naked Slash Command");
						this.slashcommands.put(sc.getCommandPrefix().get(0), sc);
					}else {
						slashcommands.addCommands(new CommandData(sc.getCommandPrefix().get(0), sc.getCommandDescription()).addOptions(sc.getCommandOptions()));
						ConsoleLogger.debug("SLASH-BUILDER", sc.getCommandPrefix().get(0)+" added Slash Command");
						this.slashcommands.put(sc.getCommandPrefix().get(0), sc);
					}
				}
			}
			slashcommands.queue();
		}
		return this;
	}
	
	public boolean performServer(String command, Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {	
		ServerCommand cmd;
		if((cmd = this.chatcommands.get(command.toLowerCase())) != null && cmd.getCommandType().isChat()) {
			switch(cmd.getCommandInfo()) {
				case DEVELOPER_COMMAND:
					if(Constants.DEVELOPERIDS.contains(member.getIdLong())) {
						cmd.performCommand(member, channel, message, guild);
						return true;
					}
					break;
				case ADMIN_COMMAND:
					if(member.hasPermission(Permission.MANAGE_CHANNEL) || member.hasPermission(Permission.ADMINISTRATOR)) {
						cmd.performCommand(member, channel, message, guild);
						return true;
					}else {
						Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "user-no-permmisions", "MANAGE_SERVER"));
					}
					break;
				case DJ_COMMAND:
					if(guildentity.isMemberDJ(member)) {
						cmd.performCommand(member, channel, message, guild);
					}else {
						Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.user-not-dj"));
					}
					return true;
				default:
					cmd.performCommand(member, channel, message, guild);
					return true;
			}
		}
		return false;
	}
	
	public boolean performServerSlash(String name, Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		ServerCommand cmd;
		if((cmd = this.slashcommands.get(name.toLowerCase())) != null && cmd.getCommandType().isSlash()) {
			switch(cmd.getCommandInfo()) {
				case DEVELOPER_COMMAND:
					if(Constants.DEVELOPERIDS.contains(member.getIdLong())) {
						cmd.performSlashCommand(member, channel, guild, event);
						return true;
					}
					break;
				case ADMIN_COMMAND:
					if(member.hasPermission(Permission.MANAGE_CHANNEL) || member.hasPermission(Permission.ADMINISTRATOR)) {
						cmd.performSlashCommand(member, channel, guild, event);
						return true;
					}else {
						Messenger.sendErrorSlashMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "user-no-permmisions", "MANAGE_SERVER"));
					}
					break;
				case DJ_COMMAND:
					if(guildentity.isMemberDJ(member)) {
						cmd.performSlashCommand(member, channel, guild, event);
					}else {
						Messenger.sendErrorSlashMessage(event, new ErrorMessageBuilder().setMessageFormat(guild, "music.user-not-dj"));
					}
					return true;
				default:
					cmd.performSlashCommand(member, channel, guild, event);
					return true;
			}
		}
		return false;
	}
}

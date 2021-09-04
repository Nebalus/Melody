package de.melody;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.melody.commands.server.ConfigCommand;
import de.melody.commands.server.info.InfoCommand;
import de.melody.commands.server.info.InviteCommand;
import de.melody.commands.server.info.PingCommand;
import de.melody.commands.server.music.BackCommand;
import de.melody.commands.server.music.FastforwardCommand;
import de.melody.commands.server.music.JoinCommand;
import de.melody.commands.server.music.LeaveCommand;
import de.melody.commands.server.music.LoopCommand;
import de.melody.commands.server.music.SkipCommand;
import de.melody.commands.server.music.PauseCommand;
import de.melody.commands.server.music.PlayCommand;
import de.melody.commands.server.music.PlaylistCommand;
import de.melody.commands.server.music.QueueCommand;
import de.melody.commands.server.music.ResumeCommand;
import de.melody.commands.server.music.RewindCommand;
import de.melody.commands.server.music.SeekCommand;
import de.melody.commands.server.music.ShuffelCommand;
import de.melody.commands.server.music.StayCommand;
import de.melody.commands.server.music.StopCommand;
import de.melody.commands.server.music.TrackinfoCommand;
import de.melody.commands.server.music.VolumeCommand;
import de.melody.commands.server.slash.PrefixCommand;
import de.melody.commands.types.ServerCommand;
import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.utils.ConsoleLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class CommandManager {

	public ConcurrentHashMap<String, ServerCommand> servercommands;
	
	public CommandManager() {
		this.servercommands = new ConcurrentHashMap<>();
		//*********************************************************
		List<ServerCommand> commandlist = List.of(
				new JoinCommand(),
				new FastforwardCommand(),
				new RewindCommand(),
				new SeekCommand(),
				new PlayCommand(),
				new PlaylistCommand(),
				new VolumeCommand(),
				new PauseCommand(),
				new ResumeCommand(),
				new StopCommand(),
				new LeaveCommand(),
				new TrackinfoCommand(),
				new QueueCommand(),
				new SkipCommand(),
				new InfoCommand(),
				new PingCommand(),
				new ConfigCommand(),
				new InviteCommand(),
				new ShuffelCommand(),
				new LoopCommand(),
				new StayCommand(),
				new BackCommand(),
				new PrefixCommand());
	
		for(ServerCommand sc : commandlist) {
			for(String subcommand : sc.getCommandPrefix()) {
				ConsoleLogger.debug("COMMAND-REGISTER", "Loading Command '"+subcommand+"'");
				this.servercommands.put(subcommand, sc);
			}
		}
		for(JDA jda : Melody.INSTANCE.shardMan.getShards()) {
			CommandListUpdateAction slashcommands = jda.updateCommands();
			for(ServerCommand sc : commandlist) {
				if(sc.isSlashCommandCompatible() || sc.getCommandType().equals(CommandType.SLASH_COMMAND)) {
					slashcommands.addCommands(new CommandData(sc.getCommandPrefix().get(0), sc.getCommandDescription()));
					ConsoleLogger.debug("SLASH-BUILDER", sc.getCommandPrefix().get(0)+" added Slash Command");
				}
			}
			slashcommands.queue();
		}
		//*********************************************************	
	}
	
	public boolean performServer(String command, Member m, TextChannel channel, Message message, Guild guild) {	
		ServerCommand cmd;
		if((cmd = this.servercommands.get(command.toLowerCase())) != null) {
			switch(cmd.getCommandType()) {
				case DEVELOPER_COMMAND:
					if(Constants.DEVELOPERIDS.contains(m.getIdLong())) {
						cmd.performCommand(m, channel, message, guild);
						return true;
					}
					break;
				case MUSIC_COMMAND:
				case INFO_COMMAND:
				case BETA_COMMAND:
					cmd.performCommand(m, channel, message, guild);
					return true;
			case SLASH_COMMAND:
				return false;
			default:
				break;
			}
		}
		return false;
	}
	
	public enum CommandType{
		MUSIC_COMMAND,
		BETA_COMMAND,
		DEVELOPER_COMMAND,
		INFO_COMMAND,
		SLASH_COMMAND;
	}
	public boolean performServerSlash(String name, Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		ServerCommand cmd;
		if((cmd = this.servercommands.get(name.toLowerCase())) != null) {
			if(cmd.isSlashCommandCompatible() || cmd.getCommandType().equals(CommandType.SLASH_COMMAND)) {
				switch(cmd.getCommandType()) {
					case DEVELOPER_COMMAND:
						if(Constants.DEVELOPERIDS.contains(member.getIdLong())) {
							cmd.performSlashCommand(member, channel, guild, event);
							return true;
						}
						break;
					case MUSIC_COMMAND:
					case INFO_COMMAND:
					case BETA_COMMAND:
					case SLASH_COMMAND:
						cmd.performSlashCommand(member, channel, guild, event);
						return true;
				}
			}
		}
		return false;
	}
}

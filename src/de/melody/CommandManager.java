package de.melody;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.melody.commands.server.ConfigCommand;
import de.melody.commands.server.info.BotInfoCommand;
import de.melody.commands.server.info.InviteCommand;
import de.melody.commands.server.info.PingCommand;
import de.melody.commands.server.music.FastforwardCommand;
import de.melody.commands.server.music.JoinCommand;
import de.melody.commands.server.music.LeaveCommand;
import de.melody.commands.server.music.LoopCommand;
import de.melody.commands.server.music.NextCommand;
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
import de.melody.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

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
				new NextCommand(),
				new BotInfoCommand(),
				new PingCommand(),
				new ConfigCommand(),
				new InviteCommand(),
				new ShuffelCommand(),
				new LoopCommand(),
				new StayCommand());
	
		for(ServerCommand sc : commandlist) {
			for(String subcommand : sc.getCommandPrefix()) {
				this.servercommands.put(subcommand, sc);
			}
		}
		//*********************************************************	
	}
	
	public boolean performServer(String command, Member m, TextChannel channel, Message message, Guild guild) {	
		ServerCommand cmd;
		if((cmd = this.servercommands.get(command.toLowerCase())) != null) {
			cmd.performCommand(m, channel, message, guild);
			return true;
		}
		return false;
	}
}

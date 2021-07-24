package de.melody;

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
import de.melody.commands.types.DirectmessageCommand;
import de.melody.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandManager {

	public ConcurrentHashMap<String, ServerCommand> servercommands;
	public ConcurrentHashMap<String, DirectmessageCommand> directmessagecommands;
	
	public CommandManager() {
		//*********************************************************
		this.servercommands = new ConcurrentHashMap<>();
		
		this.servercommands.put("join",new JoinCommand());
		this.servercommands.put("j",new JoinCommand());
		
		this.servercommands.put("fastforward",new FastforwardCommand());
		this.servercommands.put("fw",new FastforwardCommand());
		
		this.servercommands.put("rewind",new RewindCommand());
		this.servercommands.put("rw",new RewindCommand());
		
		this.servercommands.put("seek",new SeekCommand());
		
		this.servercommands.put("p",new PlayCommand());
		this.servercommands.put("play",new PlayCommand());
		
		this.servercommands.put("playlist",new PlaylistCommand());
		this.servercommands.put("pl",new PlaylistCommand());
		
		this.servercommands.put("v",new VolumeCommand());
		this.servercommands.put("volume",new VolumeCommand());
		this.servercommands.put("vol",new VolumeCommand());
		
		this.servercommands.put("pause",new PauseCommand());
		
		this.servercommands.put("resume",new ResumeCommand());
		this.servercommands.put("unpause",new ResumeCommand());
		
		this.servercommands.put("stop",new StopCommand()); 
		this.servercommands.put("l",new LeaveCommand());
		this.servercommands.put("leave",new LeaveCommand());
		
		this.servercommands.put("trackinfo",new TrackinfoCommand());
		this.servercommands.put("ti",new TrackinfoCommand());
		this.servercommands.put("nowplaying",new TrackinfoCommand());
		
		this.servercommands.put("queue",new QueueCommand());
		this.servercommands.put("q",new QueueCommand());
	
		this.servercommands.put("skip", new NextCommand());
		this.servercommands.put("s", new NextCommand());
		this.servercommands.put("next", new NextCommand());
		this.servercommands.put("n", new NextCommand());
		
		this.servercommands.put("botinfo",new BotInfoCommand());
		this.servercommands.put("ping",new PingCommand());
		this.servercommands.put("config",new ConfigCommand());
		this.servercommands.put("invite",new InviteCommand());
		
		this.servercommands.put("shuffel",new ShuffelCommand());
		
		this.servercommands.put("loop",new LoopCommand());
		
		this.servercommands.put("24/7", new StayCommand());
		this.servercommands.put("247", new StayCommand());
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
	
	public boolean performDirectmessage(String command, User user, Message message) {	
		DirectmessageCommand cmd;
		if((cmd = this.directmessagecommands.get(command.toLowerCase())) != null) {
			cmd.performCommand(command, user, message);
			return true;
		}
		return false;
	}
}

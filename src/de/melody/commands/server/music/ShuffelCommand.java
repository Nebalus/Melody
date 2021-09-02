package de.melody.commands.server.music;

import java.util.List;

import de.melody.CommandManager.CommandType;
import de.melody.commands.types.ServerCommand;
import de.melody.core.Melody;
import de.melody.music.MusicController;
import de.melody.music.Queue;
import de.melody.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class ShuffelCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		MusicController controller = melody.playerManager.getController(channel.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		if(queue.getQueuelist().size() > 1) {
			queue.shuffel(); 
			channel.sendMessage(mf.format(guild, "music.shuffel.successful",queue.getQueueSize())).queue();
		}else {
			channel.sendMessage(mf.format(guild, "music.shuffel.emptyqueue")).queue();
		}
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("shuffel");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.MUSIC_COMMAND;
	}
	@Override
	public boolean isSlashCommandCompatible() {
		return false;
	}
	@Override
	public String getCommandDescription() {
		return "Shuffels the queue";
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}
}

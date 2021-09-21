package de.melody.commands.music;

import java.util.List;

import de.nebalus.botbuilder.command.CommandInfo;
import de.nebalus.botbuilder.command.CommandType;
import de.melody.core.Melody;
import de.melody.music.MusicController;
import de.melody.music.Queue;
import de.melody.speechpackets.MessageFormatter;
import de.nebalus.botbuilder.command.ServerCommand;
import de.nebalus.botbuilder.utils.Messenger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class ShuffleCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		MusicController controller = melody.playerManager.getController(guild.getIdLong());
		Queue queue = controller.getQueue();
		if(queue.getQueuelist().size() > 1) {
			queue.shuffel(); 
			Messenger.sendMessageEmbed(channel, mf.format(guild, "music.shuffel.successful",queue.getQueueSize())).queue();
		}else {
			Messenger.sendMessageEmbed(channel, mf.format(guild, "music.shuffel.emptyqueue")).queue();
		}
	}
	
	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		MusicController controller = melody.playerManager.getController(guild.getIdLong());
		Queue queue = controller.getQueue();
		if(queue.getQueuelist().size() > 1) {
			queue.shuffel(); 
			event.replyEmbeds(Messenger.getMessageEmbed(guild, mf.format(guild, "music.shuffel.successful",queue.getQueueSize()))).queue();
		}else {
			event.replyEmbeds(Messenger.getMessageEmbed(guild, mf.format(guild, "music.shuffel.emptyqueue"))).queue();
		}
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("shuffle");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.INFO_COMMAND;
	}
	@Override
	public String getCommandDescription() {
		return "Shuffles the queue";
	}

	@Override
	public List<OptionData> getCommandOptions() {
		return null;
	}
}

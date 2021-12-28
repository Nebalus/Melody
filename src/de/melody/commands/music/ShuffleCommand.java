package de.melody.commands.music;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.music.MusicController;
import de.melody.music.Queue;
import de.melody.speechpackets.MessageFormatter;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import de.melody.tools.commandbuilder.ServerCommand;
import de.melody.tools.messenger.Messenger;
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
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		MusicController controller = melody.playerManager.getController(guild.getIdLong());
		Queue queue = controller.getQueue();
		if(queue.getQueuelist().size() > 1) {
			queue.shuffle(); 
			Messenger.sendMessageEmbed(channel, mf.format(guild, "music.shuffle.successful",queue.getQueueSize())).queue();
		}else {
			Messenger.sendMessageEmbed(channel, mf.format(guild, "music.shuffle.emptyqueue")).queue();
		}
	}
	
	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		MusicController controller = melody.playerManager.getController(guild.getIdLong());
		Queue queue = controller.getQueue();
		if(queue.getQueuelist().size() > 1) {
			queue.shuffle(); 
			event.replyEmbeds(Messenger.getMessageEmbed(mf.format(guild, "music.shuffle.successful",queue.getQueueSize()))).queue();
		}else {
			event.replyEmbeds(Messenger.getMessageEmbed(mf.format(guild, "music.shuffle.emptyqueue"))).queue();
		}
	}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"shuffle"};
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}

	
	@Override
	public String getCommandDescription() {
		return "Shuffles the queue";
	}

	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}
	
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.DJ;
	}
}

package de.melody.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.nebalus.botbuilder.command.CommandInfo;
import de.nebalus.botbuilder.command.CommandType;
import de.melody.core.Melody;
import de.melody.music.MusicController;
import de.melody.music.Queue;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
import de.melody.utils.Utils;
import de.nebalus.botbuilder.command.ServerCommand;
import de.nebalus.botbuilder.utils.Messenger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class BackCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		GuildVoiceState state;
		EmbedBuilder builder = new EmbedBuilder();
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			String[] args = message.getContentDisplay().split(" ");
			AudioPlayer player = controller.getPlayer();
			Queue queue = controller.getQueue();
			if(player.getPlayingTrack() != null) {
				player.stopTrack();
				builder.setDescription(Emoji.PREVIOUS_TITLE+" test");
				Messenger.sendMessageEmbed(channel, builder).queue();
				try {
					int i = Integer.valueOf(args[1]);
					queue.back(i);
				}catch(NumberFormatException | IndexOutOfBoundsException e) {
					queue.back(1);
				}
			}else 
				Utils.sendErrorEmbled(message, mf.format(guild, "feedback.music.currently-playing-null"),m);
		}else
			Utils.sendErrorEmbled(message, mf.format(guild, "feedback.music.bot-not-in-vc"), m);
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("back","b");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT_COMMAND;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.INFO_COMMAND;
	}

	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}
	
	@Override
	public List<OptionData> getCommandOptions() {
		return null;
	}
}

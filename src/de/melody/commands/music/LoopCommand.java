package de.melody.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.nebalus.botbuilder.command.CommandInfo;
import de.nebalus.botbuilder.command.CommandType;
import de.melody.core.Melody;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
import de.nebalus.botbuilder.command.ServerCommand;
import de.nebalus.botbuilder.utils.Messenger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class LoopCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {

		GuildVoiceState state;
		if((state = m.getVoiceState()) != null && state.getChannel() != null) {
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			AudioPlayer player = controller.getPlayer();
			if(player.getPlayingTrack() != null) {
				if(controller.isLoop()) {
					controller.setLoop(false);
					Messenger.sendMessageEmbed(channel,Emoji.SINGLE_LOOP+mf.format(guild, "music.info.loop-disabled")).queue();
				}else {
					controller.setLoop(true);
					Messenger.sendMessageEmbed(channel,Emoji.SINGLE_LOOP+mf.format(guild, "music.info.loop-enabled")).queue();
				}	
			}else 
				MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.music.currently-playing-null"));				
		}else 
			MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.music.bot-not-in-vc"));
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("loop");
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
		// TODO Auto-generated method stub
		return null;
	}
}
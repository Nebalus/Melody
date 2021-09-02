package de.melody.commands.server.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.CommandManager.CommandType;
import de.melody.commands.types.ServerCommand;
import de.melody.core.Melody;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.music.Queue;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class StopCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		melody.entityManager.getGuildEntity(guild).setChannelId(channel.getIdLong());
		MusicController controller = melody.playerManager.getController(guild.getIdLong());
		AudioPlayer player = controller.getPlayer();
		GuildVoiceState state;
		if((state = m.getVoiceState()) != null && state.getChannel() != null) {
			if(player.getPlayingTrack() != null) {
				Queue queue = controller.getQueue();
				player.stopTrack();
				queue.clear();
				melody.playerManager.getController(guild.getIdLong()).setAfkTime(600);
				message.addReaction(Emoji.OK_HAND).queue();
			}else 
				MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.music.currently-playing-null"));
		}else 
			MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.music.user-not-in-vc"));
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("stop");
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
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}
}

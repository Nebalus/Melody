package de.melody.commands.server.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.music.Queue;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emojis;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class StopCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@SuppressWarnings("unused")
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		melody.entityManager.getGuildEntity(guild.getIdLong()).setChannelId(channel.getIdLong());
		MusicController controller = melody.playerManager.getController(guild.getIdLong());
		AudioPlayer player = controller.getPlayer();
		GuildVoiceState state;
		VoiceChannel vc;
		if((state = m.getVoiceState()) != null && (vc = state.getChannel()) != null) {
			if(player.getPlayingTrack() != null) {
				Queue queue = controller.getQueue();
				player.stopTrack();
				queue.clear();
				melody.playerManager.getController(guild.getIdLong()).setAfkTime(600);
				message.addReaction(Emojis.OK_HAND).queue();
			}else 
				MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "feedback.music.currently-playing-null"));
		}else 
			MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "feedback.music.user-not-in-vc"));
	}
}

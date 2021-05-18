package de.melody.commands.server.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		melody.entityManager.getGuildEntity(guild.getIdLong()).setChannelId(channel.getIdLong());
		GuildVoiceState state;
		VoiceChannel vc;
		if((state = m.getVoiceState()) != null && (vc = state.getChannel()) != null) {
			AudioManager manager = guild.getAudioManager();
			manager.openAudioConnection(vc);
			MusicController controller = melody.playerManager.getController(guild.getIdLong());
			AudioPlayer player = controller.getPlayer();
			player.setPaused(false);
			if(player.getPlayingTrack() == null) {
				controller.setAfkTime(600);
			}
		}else {
			MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "feedback.music.user-not-in-vc"));		
		}
	}
}

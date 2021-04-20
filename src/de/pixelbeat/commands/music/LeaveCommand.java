package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements ServerCommand{

	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		MusicUtil.updateChannel(channel);
		GuildVoiceState state;
		VoiceChannel vc;
		if((state = m.getGuild().getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
			MusicController controller = PixelBeat.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
			AudioManager manager = vc.getGuild().getAudioManager();
			AudioPlayer player = controller.getPlayer();
				
			player.stopTrack();
			manager.closeAudioConnection();
			message.addReaction("U+1F44C").queue();
		}else {
			MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), mf.format(channel.getGuild().getIdLong(), "feedback.music.bot-not-in-vc"));
		}
	}
}

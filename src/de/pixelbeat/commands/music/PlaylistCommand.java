package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.AudioLoadResult;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.speechpackets.MessageFormatter;
import de.pixelbeat.utils.Utils;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlaylistCommand implements ServerCommand{
	
	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ");
		MusicUtil.updateChannel(channel);
		if(args.length > 1) {
			GuildVoiceState state;
			VoiceChannel vc;
			if((state = m.getVoiceState()) != null && (vc = state.getChannel()) != null) {
				MusicController controller = PixelBeat.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
				AudioPlayerManager apm = PixelBeat.INSTANCE.audioPlayerManager;
				AudioManager manager = vc.getGuild().getAudioManager();
				StringBuilder strBuilder = new StringBuilder();
				for(int i = 1; i < args.length; i++) strBuilder.append(args[i] + " ");
				String url = strBuilder.toString().trim();		
				if(MusicUtil.isUrlVerified(url)) {
					manager.openAudioConnection(vc);
					final String uri = url;
					apm.loadItem(uri, new AudioLoadResult(controller, uri, m, true, false, false));
				}else {
					MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), mf.format(channel.getGuild().getIdLong(), "feedback.music.error.non-whitelisted-domain",MusicUtil.getDomaene(url)));
				}					
			}else {
				MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), mf.format(channel.getGuild().getIdLong(), "feedback.music.user-not-in-vc"));
			}
		}else {
			MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), "Please use "+Utils.getGuildPrefix(channel.getGuild().getIdLong())+"playlist <url>");
		}
	}
}

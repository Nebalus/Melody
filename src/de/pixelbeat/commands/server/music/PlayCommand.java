package de.pixelbeat.commands.server.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.entities.GuildEntity;
import de.pixelbeat.music.AudioLoadResult;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ServerCommand{

	private PixelBeat pixelbeat = PixelBeat.INSTANCE;
	private MessageFormatter mf = pixelbeat.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		GuildEntity guildentity = pixelbeat.entityManager.getGuildEntity(guild.getIdLong());
		guildentity.setChannelId(channel.getIdLong());
		if(args.length > 1) {
			GuildVoiceState state;
			VoiceChannel vc;
			if((state = m.getVoiceState()) != null && (vc = state.getChannel()) != null) {
				MusicController controller = pixelbeat.playerManager.getController(guild.getIdLong());
				AudioPlayerManager apm = pixelbeat.audioPlayerManager;
				AudioManager manager = guild.getAudioManager();
				StringBuilder strBuilder = new StringBuilder();
				for (int i = 1; i < args.length; i++) strBuilder.append(args[i] + " ");
				String url = strBuilder.toString().trim();
				boolean isytsearch = false;
				if(!url.startsWith("http")) {
					url = "ytsearch: " + url;
					isytsearch = true;
				}
				if(MusicUtil.isUrlVerified(url) || isytsearch == true) {
					manager.openAudioConnection(vc);
					final String uri = url;
					apm.loadItem(uri, new AudioLoadResult(controller, uri, m, false, false, false));
				}else {
					MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "feedback.music.error.non-whitelisted-domain",MusicUtil.getDomain(url)));
				}					
			}else {
				MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "feedback.music.user-not-in-vc"));
			}
		}else {
			MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "feedback.info.command-usage",guildentity.getPrefix()+"play <url/search query>"));
		}
	}
}

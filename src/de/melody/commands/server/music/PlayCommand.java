package de.melody.commands.server.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.entities.GuildEntity;
import de.melody.music.AudioLoadResult;
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

public class PlayCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		String[] args = message.getContentDisplay().split(" ");
		GuildEntity guildentity = melody.entityManager.getGuildEntity(guild.getIdLong());
		guildentity.setChannelId(channel.getIdLong());
		if(args.length > 1) {
			GuildVoiceState state;
			VoiceChannel vc;
			if((state = m.getVoiceState()) != null && (vc = state.getChannel()) != null) {
				MusicController controller = melody.playerManager.getController(guild.getIdLong());
				AudioPlayerManager apm = melody.audioPlayerManager;
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

package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.AudioLoadResult;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.utils.Emojis;
import de.pixelbeat.utils.Misc;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ");
		MusicUtil.updateChannel(channel);
		
			if(args.length > 1) {
				GuildVoiceState state;
				if((state = m.getVoiceState()) != null) {
					VoiceChannel vc;
					if((vc = state.getChannel()) != null) {
						MusicController controller = PixelBeat.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
						AudioPlayerManager apm = PixelBeat.INSTANCE.audioPlayerManager;
						AudioManager manager = vc.getGuild().getAudioManager();
						StringBuilder strBuilder = new StringBuilder();
						for(int i = 1; i < args.length; i++) strBuilder.append(args[i] + " ");
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
							EmbedBuilder builder = new EmbedBuilder();
							builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" The domain **"+MusicUtil.getDomaene(url)+"** is not whitelisted in my system.");
							MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);
						}		
						
					}else {
						EmbedBuilder builder = new EmbedBuilder();
						builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" You are probably not in a voice channel.");
						MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);
					}
				}else {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" You are probably not in a voice channel.");
					MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);
				}
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" Please use "+Misc.getGuildPrefix(channel.getGuild().getIdLong())+"play <url/search query>");
				MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);
			}
	}

}

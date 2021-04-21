package de.pixelbeat.music;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.pixelbeat.LiteSQL;
import de.pixelbeat.PixelBeat;
import de.pixelbeat.speechpackets.MessageFormatter;
import de.pixelbeat.utils.Emojis;
import de.pixelbeat.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class TrackScheduler extends AudioEventAdapter{

	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void onPlayerPause(AudioPlayer player) {
		long guildid = PixelBeat.INSTANCE.playerManager.getGuildByPlayerHash(player.hashCode());
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(Emojis.PAUSE+" "+mf.format(guildid, "music.track.pause"));
		MusicUtil.sendEmbled(guildid, builder);		
	}
	
	@Override
	public void onPlayerResume(AudioPlayer player) {
		long guildid = PixelBeat.INSTANCE.playerManager.getGuildByPlayerHash(player.hashCode());
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(Emojis.RESUME+" "+mf.format(guildid, "music.track.resume"));
		MusicUtil.sendEmbled(guildid, builder);		
	}
	
	
	
	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		long guildid = PixelBeat.INSTANCE.playerManager.getGuildByPlayerHash(player.hashCode());
		Guild guild = PixelBeat.INSTANCE.shardMan.getGuildById(guildid);
		MusicController controller = PixelBeat.INSTANCE.playerManager.getController(guildid);
		Queue queue = controller.getQueue();
		if(queue.isLoop() == false && queue.isLoopQueue() == false) {
			EmbedBuilder builder = new EmbedBuilder();
			AudioTrackInfo info = track.getInfo();
			builder.setDescription(guild.getJDA().getEmoteById(Emojis.ANIMATED_PLAYING).getAsMention()+" "+mf.format(guildid, "music.track.currently-playing")+ info.title);
			String url = info.uri;
			builder.addField("**"+info.author+"**","[" + info.title+"]("+url+")", false);
			builder.addField(mf.format(guildid, "music.track.length"), MusicUtil.getTime(info,0l),true);
			Member m = queue.currentplaying.getWhoQueued();
			builder.setFooter(mf.format(guildid, "music.user.who-requested")+ m.getUser().getAsTag());
			builder.setTimestamp(OffsetDateTime.now());
			if(url.startsWith("https://www.youtube.com/watch?v=")) {
				String videoID = url.replace("https://www.youtube.com/watch?v=", "");
			
				builder.setImage("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
				//builder.setThumbnail("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
				MusicUtil.sendEmbled(guildid, builder);							
			}else {
				MusicUtil.sendEmbled(guildid, builder);
			}

			VoiceChannel vc;
			if((vc = controller.getGuild().getSelfMember().getVoiceState().getChannel()) != null) {
				for(Member vcm : vc.getMembers()) {
					if(!vcm.getUser().isBot()) {
						if(!Utils.doesUserExist(vcm.getIdLong())) {
							try {
								PreparedStatement ps = LiteSQL.getConnection().prepareStatement("INSERT INTO userdata(userid) VALUES(?)");
								ps.setLong(1, vcm.getIdLong());
								ps.executeUpdate();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {	
		long guildid = PixelBeat.INSTANCE.playerManager.getGuildByPlayerHash(player.hashCode());
		Guild guild = PixelBeat.INSTANCE.shardMan.getGuildById(guildid);
		MusicController controller = PixelBeat.INSTANCE.playerManager.getController(guildid);
		Queue queue = controller.getQueue();
		if(endReason.mayStartNext) {
			
			GuildVoiceState state;
			if((state = guild.getSelfMember().getVoiceState()) != null) {
				VoiceChannel vc;
				if((vc = state.getChannel()) != null) {
					if(!queue.isLoop()) {
						if(!queue.isLoopQueue()) {
							if(!queue.next()) { 
								if(vc.getMembers().size() > 1) {
									MusicUtil.getVoiceAfkTime.put(guildid, 600l);
								}
							}else {
								return;
							}
						}else {
							AudioPlayerManager apm = PixelBeat.INSTANCE.audioPlayerManager;
							final String uri = track.getInfo().uri;
							apm.loadItem(uri, new AudioLoadResult(controller, uri, null, false, false, true));
							return;
						}
					}else {
						AudioPlayerManager apm = PixelBeat.INSTANCE.audioPlayerManager;
						final String uri = track.getInfo().uri;
						apm.loadItem(uri, new AudioLoadResult(controller, uri, null, false, true, false));
						return;
					}
				}
			}
		}
		if(queue.isLoop()) {
			queue.setLoop(false);	
		}
		player.stopTrack();
	}
	
	  @Override
	  public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {

	  }
	  
	  @Override
	  public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		 long guildid = PixelBeat.INSTANCE.playerManager.getGuildByPlayerHash(player.hashCode());
		 EmbedBuilder builder = new EmbedBuilder();
		 builder.setDescription("An error occured.");
		 builder.addField("Errorcode",exception.getMessage()+"", false);
		 MusicUtil.sendEmbled(guildid, builder);
	 }
}

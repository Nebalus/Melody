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
import de.pixelbeat.utils.Emojis;
import de.pixelbeat.utils.Misc;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class TrackScheduler extends AudioEventAdapter{

	
	@Override
	public void onPlayerPause(AudioPlayer player) {
		long guildid = PixelBeat.INSTANCE.playerManager.getGuildByPlayerHash(player.hashCode());
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(Emojis.PAUSE+" Bot paused");
		MusicUtil.sendEmbled(guildid, builder);		
	}
	
	@Override
	public void onPlayerResume(AudioPlayer player) {
		long guildid = PixelBeat.INSTANCE.playerManager.getGuildByPlayerHash(player.hashCode());
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(Emojis.RESUME+" Bot resumed");
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
			Member m;
			m = queue.getuserwhoqueued(0);
			builder.setDescription(guild.getJDA().getEmoteById(Emojis.ANIMATED_PLAYING).getAsMention()+" Currently playing: "+ info.title);
			
			String url = info.uri;
			builder.addField("**"+info.author+"**","[" + info.title+"]("+url+")", false);
			builder.addField("**Length**", MusicUtil.getTime(info,0l),true);
			//builder.addField("Position in the queue", queue.getcurrentqueuesize()+"" ,true);
			
			builder.setFooter("Requested by "+ m.getUser().getAsTag());
			builder.setTimestamp(OffsetDateTime.now());
			if(url.startsWith("https://www.youtube.com/watch?v=")) {
				String videoID = url.replace("https://www.youtube.com/watch?v=", "");
			
				//builder.setImage("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
				builder.setThumbnail("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
				MusicUtil.sendEmbled(guildid, builder);							
			}else {
				MusicUtil.sendEmbled(guildid, builder);
			}
				
			VoiceChannel vc;
			if((vc = controller.getGuild().getSelfMember().getVoiceState().getChannel()) != null) {
				for(Member vcm : vc.getMembers()) {
					if(!vcm.getUser().isBot()) {
						if(!Misc.doesUserExist(vcm.getIdLong())) {
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

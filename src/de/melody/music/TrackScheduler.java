package de.melody.music;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.melody.Melody;
import de.melody.entities.reacts.TrackReaction;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emojis;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class TrackScheduler extends AudioEventAdapter{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void onPlayerPause(AudioPlayer player) {
		long guildid = melody.playerManager.getGuildByPlayerHash(player.hashCode());
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(Emojis.PAUSE+" "+mf.format(guildid, "music.track.pause"));
		MusicUtil.sendEmbled(guildid, builder);		
	}
	
	@Override
	public void onPlayerResume(AudioPlayer player) {
		long guildid = melody.playerManager.getGuildByPlayerHash(player.hashCode());
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(Emojis.RESUME+" "+mf.format(guildid, "music.track.resume"));
		MusicUtil.sendEmbled(guildid, builder);		
	}
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		long guildid = melody.playerManager.getGuildByPlayerHash(player.hashCode());
		Guild guild = melody.shardMan.getGuildById(guildid);
		MusicController controller = melody.playerManager.getController(guildid);
		Queue queue = controller.getQueue();
		if(controller.isLoop() == false && controller.isLoopQueue() == false) {
			EmbedBuilder builder = new EmbedBuilder();
			AudioTrackInfo info = track.getInfo();
			builder.setDescription(guild.getJDA().getEmoteById(Emojis.ANIMATED_PLAYING).getAsMention()+" "+mf.format(guildid, "music.track.currently-playing")+ info.title);
			String url = info.uri;
			builder.addField("**"+info.author+"**","[" + info.title+"]("+url+")", false);
			builder.addField(mf.format(guildid, "music.track.length"), MusicUtil.getTime(info,0l),true);
			builder.setFooter(mf.format(guildid, "music.user.who-requested")+ queue.currentplaying.getWhoQueued().getUser().getAsTag());
			builder.setColor(Melody.HEXEmbeld);
			if(url.startsWith("https://www.youtube.com/watch?v=")) {
				String videoID = url.replace("https://www.youtube.com/watch?v=", "");
				builder.setImage("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
				//builder.setThumbnail("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");		
			}
			MusicUtil.getChannel(guildid).sendMessage(builder.build()).queue((trackmessage) ->{
				TrackReaction te = new TrackReaction(info);
				melody.entityManager.getGuildController(guild.getIdLong()).getReactionManager().addReactionMessage(trackmessage.getIdLong(), te);
				trackmessage.addReaction(Emojis.SPARKLING_HEART).queue();
			});
	
			VoiceChannel vc;
			if((vc = controller.getGuild().getSelfMember().getVoiceState().getChannel()) != null) {
				for(Member vcm : vc.getMembers()) {
					if(!vcm.getUser().isBot()) {
						if(!Utils.doesUserExist(vcm.getIdLong())) {
							try {
								PreparedStatement ps = melody.getDatabase().getConnection().prepareStatement("INSERT INTO userdata(userid) VALUES(?)");
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
		long guildid = melody.playerManager.getGuildByPlayerHash(player.hashCode());
		Guild guild = melody.shardMan.getGuildById(guildid);
		MusicController controller = melody.playerManager.getController(guildid);
		Queue queue = controller.getQueue();
		if(endReason.mayStartNext) {	
			GuildVoiceState state;
			if((state = guild.getSelfMember().getVoiceState()) != null) {
				VoiceChannel vc;
				if((vc = state.getChannel()) != null) {
					if(!controller.isLoop()) {
						if(!controller.isLoopQueue()) {
							if(!queue.next()) { 
								if(vc.getMembers().size() > 1) {
									controller.setAfkTime(600);
								}
							}else {
								return;
							}
						}else {
							AudioPlayerManager apm = melody.audioPlayerManager;
							final String uri = track.getInfo().uri;
							apm.loadItem(uri, new AudioLoadResult(controller, uri, null, false, false, true));
							return;
						}
					}else {
						AudioPlayerManager apm = melody.audioPlayerManager;
						final String uri = track.getInfo().uri;
						apm.loadItem(uri, new AudioLoadResult(controller, uri, null, false, true, false));
						return;
					}
				}
			}
		}
		if(controller.isLoop()) {
			controller.setLoop(false);	
		}
		player.stopTrack();
	}
	
	  @Override
	  public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {

	  }
	  
	  @Override
	  public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		 long guildid = melody.playerManager.getGuildByPlayerHash(player.hashCode());
		 EmbedBuilder builder = new EmbedBuilder();
		 builder.setDescription("An error occured.");
		 builder.addField("Errormessage",exception.getMessage(), false);
		 builder.addField("Errorcode",exception.getCause()+"", false);
		 MusicUtil.sendEmbled(guildid, builder);
	 }
}

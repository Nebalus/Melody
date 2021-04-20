package de.pixelbeat.music;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.pixelbeat.ConsoleLogger;
import de.pixelbeat.LiteSQL;
import de.pixelbeat.PixelBeat;
import de.pixelbeat.utils.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.sharding.ShardManager;


public class MusicUtil extends ListenerAdapter{

	public static HashMap<Long, Long> getVoiceAfkTime = new HashMap<Long, Long>();
	
	public static ArrayList<String> verifiedurl = new ArrayList<String>();
	
	public static void updateChannel(TextChannel channel) {	
		try {
			ResultSet set = LiteSQL.onQuery("SELECT channelid FROM general WHERE guildid = " + channel.getGuild().getIdLong());
			if(set.next()) {
				if(set.getLong("channelid") != channel.getIdLong()) {
					LiteSQL.onUpdate("UPDATE general SET channelid = " + channel.getIdLong() + " WHERE guildid = "+ channel.getGuild().getIdLong());
				}
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}	
	}
	
	private static TextChannel getChannel(long guildid) {
		ResultSet set = LiteSQL.onQuery("SELECT channelid FROM general WHERE guildid = "+guildid);
		try {
			if(set.next()) {
				long channelid = set.getLong("channelid");
				Guild guild;
				if((guild = PixelBeat.INSTANCE.shardMan.getGuildById(guildid)) != null) {
					TextChannel channel;
					if((channel = guild.getTextChannelById(channelid)) != null) {
						return channel;
					}
				}
			}
		}catch(SQLException ex) {
			ex.printStackTrace();	
		}
		return null;
	}
	
	public static void sendEmbled(long guildid, EmbedBuilder builder) {		
		TextChannel channel;
		if((channel = getChannel(guildid)) != null) {
			builder.setColor(PixelBeat.HEXEmbeld);
			channel.sendMessage(builder.build()).queue();
		}			
	}
	
	public static void sendEmbledError(long guildid, String errormessage) {
		TextChannel channel;
		if((channel = getChannel(guildid)) != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" "+errormessage);
			builder.setColor(PixelBeat.HEXEmbeldError);
			channel.sendMessage(builder.build()).queue();
		}				
	}
	
	public static int getVolume(Long GuildID) {
		try {
			ResultSet rs = LiteSQL.onQuery("SELECT volume FROM general WHERE guildid = " + GuildID);	
			if(rs.next()) {
				if(rs.getInt("volume") != 0) {
					return rs.getInt("volume");	
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 50;
	}
	
	public static Boolean setVolume(Long GuildID, int volume) {
		ResultSet set = LiteSQL.onQuery("SELECT volume FROM general WHERE guildid = " + GuildID);
		try {
			if(set.next()) {
			 LiteSQL.onUpdate("UPDATE general SET volume = " + volume + " WHERE guildid = "+ GuildID);
			 return true;	
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		VoiceChannel vc = event.getChannelLeft();
		if(event.getMember() == event.getGuild().getSelfMember()) {	
			MusicKiller(event.getGuild());
		}else {
			if(vc.getMembers().contains(event.getGuild().getSelfMember())) {
				if(vc.getMembers().size() == 1) {
					getVoiceAfkTime.put(event.getGuild().getIdLong(), 240l);
				}
			}
		}
		
	}
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		MusicController controller = PixelBeat.INSTANCE.playerManager.getController(event.getGuild().getIdLong());
		if(event.getMember() == event.getGuild().getSelfMember()) {
			AudioPlayer player = controller.getPlayer();
			player.setPaused(false);
			event.getGuild().getSelfMember().deafen(true).queue();
		}
	}
	
	@Override
	public void onGuildVoiceDeafen(GuildVoiceDeafenEvent event) {
		if(event.getMember() == event.getGuild().getSelfMember()) {
			if(!event.isDeafened()) {
				event.getGuild().getSelfMember().deafen(true).queue();
			}
		}
	}
	
	public static void onRefreshAutoDisabler(ShardManager shardMan) {
		try {
			for(Guild g :  shardMan.getGuilds()) {
				GuildVoiceState state;
				if((state = g.getSelfMember().getVoiceState()) != null) {
					VoiceChannel vc;
					if((vc = state.getChannel()) != null) {
						MusicController controller = PixelBeat.INSTANCE.playerManager.getController(g.getIdLong());
						AudioPlayer player = controller.getPlayer();
						if(player.getPlayingTrack() != null) {
							if(vc.getMembers().size() == 1) {
								AFKManager(g);
							}
						}else {
							AFKManager(g);
						}
					}else {
						AFKManager(g);
					}
				}
			}
		}catch(IllegalStateException e) {}
	}
	
	public static void AFKManager(Guild g) {
		if(getVoiceAfkTime.containsKey(g.getIdLong())) {
			Long time = getVoiceAfkTime.get(g.getIdLong());
			if(time != 0) {
				time--;
				ConsoleLogger.debug(g.getIdLong()+"", time+"");
				getVoiceAfkTime.put(g.getIdLong(), time);
			}else {
				MusicKiller(g);
			}
		}
	}
	
	public static void MusicKiller(Guild g) {
		MusicController controller = PixelBeat.INSTANCE.playerManager.getController(g.getIdLong());
		AudioPlayer player = controller.getPlayer();
		GuildVoiceState state;
		VoiceChannel vc;
		
		player.stopTrack();
		if((state = g.getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
			AudioManager manager = vc.getGuild().getAudioManager();
			manager.closeAudioConnection();
		}
		PixelBeat.INSTANCE.playerManager.clearController(g.getIdLong());
		getVoiceAfkTime.remove(g.getIdLong());
	}
	
	public static String getTime(AudioTrackInfo trackinfo, Long time) {
		if(trackinfo != null) {
			long sekunden = trackinfo.length/1000;
			long minuten = sekunden/60;
			long stunden = minuten/60;
			sekunden %= 60;
			minuten %= 60;	
			return trackinfo.isStream ? ":red_circle: STREAM" : (stunden > 0 ? stunden +"h " : "")+(minuten == 0 && stunden == 0? "" : minuten +"min ")+sekunden+"s";
		}else if(time >= 1){
			long sekunden = time/1000;
			long minuten = sekunden/60;
			long stunden = minuten/60;
			sekunden %= 60;
			minuten %= 60;	
			return (stunden > 0 ? stunden +"h " : "")+(minuten == 0 && stunden == 0? "" : minuten +"min ")+sekunden+"s";
		}
		return null;
	}
	
	public static Long getTimeUntil(MusicController controller) {
		Queue queue = controller.getQueue();
		AudioPlayer player = controller.getPlayer();
		Long timeUntil = 0l;
		for(QueuedTrack track1 : queue.getQueuelist()) {
			timeUntil = timeUntil + track1.getTrack().getDuration();
		}
		timeUntil = timeUntil + player.getPlayingTrack().getDuration() - player.getPlayingTrack().getPosition();
		return timeUntil;
	}
	
	public static Boolean isUrlVerified(String url) {
		String uri = getDomaene(url);
		if(uri != null) {
			if(verifiedurl.contains(uri)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getDomaene(String url) {
		if(url.startsWith("http://") || url.startsWith("https://")) {
			String[] args = url.split("/");
			return args[2].toLowerCase();
		}
		return null;
	}
	
	public static void loadDomains() {
		verifiedurl.add("www.youtube.com");	
		verifiedurl.add("music.youtube.com");	
		verifiedurl.add("youtu.be");	
		verifiedurl.add("youtube.com");			
		verifiedurl.add("www.twitch.tv");	
		verifiedurl.add("vimeo.com");		
		verifiedurl.add("bandcamp.com");		
		verifiedurl.add("soundcloud.com");
	}
}

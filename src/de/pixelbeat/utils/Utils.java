package de.pixelbeat.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.pixelbeat.PixelBeat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class Utils {
	
	public static boolean doesGuildExist(Long GuildId) {
		try {
			ResultSet rs = PixelBeat.INSTANCE.getDatabase().onQuery("SELECT guildid FROM general WHERE guildid = " + GuildId);
			if(rs.next()) {
				return true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean doesUserExist(Long UserId) {
		try {
			ResultSet rs = PixelBeat.INSTANCE.getDatabase().onQuery("SELECT userid FROM userdata WHERE userid = " + UserId);
			if(rs.next()) {
				return true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Long getUserInt() {
		try {
			ResultSet rs = PixelBeat.INSTANCE.getDatabase().onQuery("SELECT seq FROM sqlite_sequence WHERE name = \"userdata\"");
			if(rs.next()) {
				if(rs.getString("seq") != null) {
					return rs.getLong("seq");	
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return 0l;
	}
	
	public static String uptime(long time) {
		long uptime = time;
		
		long sekunden = uptime;
		long minuten = sekunden/60;
		long stunden = minuten/60;
		long tage = stunden/24;
		stunden %= 24;
		minuten %= 60;
		sekunden %= 60;
		
		String uptimeSuffix = null;
		//uptimeSuffix = "just started";
		if(uptime == 0) {
			uptimeSuffix = "0s";
		}
		if(sekunden >= 1) {
			uptimeSuffix = sekunden +"s";
		}
		if(uptime >= 60) {
			uptimeSuffix = minuten +"min "+(uptimeSuffix != null ? uptimeSuffix : "");
		}
		if(uptime >= 3600) {
			uptimeSuffix = stunden +"h "+(uptimeSuffix != null ? uptimeSuffix : "");
		}
		if(uptime >= 86400) {
			uptimeSuffix = tage +"d "+(uptimeSuffix != null ? uptimeSuffix : "");
		}
		
		String replace = uptimeSuffix.replace(" ", ", ");
		return replace;
	}
	
	public static String getTimeFormat(Long time) {		
		if(time >= 1000) {
			long sekunden = time/1000;
			long minuten = sekunden/60;
			long stunden = minuten/60;
			sekunden %= 60;
			minuten %= 60;
			String timeformat = "";
			
			if(stunden > 0) {
				if(stunden <= 9) {
					timeformat = timeformat + "0"+stunden+":";
				}else timeformat = timeformat + stunden+":";	
			}
			
			if(minuten <= 9) {
				timeformat = timeformat + "0"+minuten+":";
			}else timeformat = timeformat + minuten+":";	
			
			if(sekunden <= 9) {
				timeformat = timeformat + "0"+sekunden;
			}else timeformat = timeformat + sekunden;
			
			return timeformat;
		}
		return null;
	}
	
	public static void sendErrorEmbled(TextChannel channel, String discription, Member m) {				
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" "+m.getUser().getAsMention()+" "+discription);
		builder.setColor(PixelBeat.HEXEmbeldError);
		channel.sendMessage(builder.build()).queue();
	}
	
	public static void setUserlistenTime(Long UserID, Long listentime) {
		//Würde eine JSON Codierung Empfelen
	}
	
	public static Long getUserlistenTime(Long UserID) {
		//Würde eine JSON Codierung Empfelen
		return 0l;
	}

}
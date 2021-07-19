package de.melody.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.Melody;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class Utils {
	
	public static boolean doesGuildExist(Long GuildId) {
		try {
			ResultSet rs = Melody.INSTANCE.getDatabase().onQuery("SELECT guildid FROM guilds WHERE guildid = " + GuildId);
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
			ResultSet rs = Melody.INSTANCE.getDatabase().onQuery("SELECT userid FROM userdata WHERE userid = " + UserId);
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
			ResultSet rs = Melody.INSTANCE.getDatabase().onQuery("SELECT seq FROM sqlite_sequence WHERE name = \"userdata\"");
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
	
	public static String decodeStringFromTimeMillis(long time,Boolean inseconds) {
		long uptime;
		if(inseconds) {
			uptime = time;
		}else {
			uptime = time/1000;
		}
		
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
	public static Boolean isStringValidBoolean(String value) {
		Boolean bool = false;
		value = value.toLowerCase();
		switch(value) {
			case "true":
			case "on":
			case "false":
			case "off":
				bool =true;
				break;
		}
		return bool;
	}
	public static Boolean getBooleanFromString(String value) {
		Boolean bool = false;
		value = value.toLowerCase();
		switch(value) {
			case "true":
			case "on":
				bool =true;
				break;
			case "false":
			case "off":
				bool =false;
				break;
		}
		return bool;
	}
	
	public static String getStringFromBoolean(Boolean value) {
		if(value) {
			return "on";
		}else {
			return "off";
		}
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
	
	public static Long decodeTimeMillisFromString(String time) {
		Long endTime = 0l;
		for(String args : time.split(" ")) {
			args = args.toLowerCase();
			if(args.endsWith("sec")) {
				args = args.replace("sec", "");
				int seconds = Integer.valueOf(args);
				if (seconds < 0) {
					seconds *= -1;
				}
				endTime = endTime + (seconds*1000);
			}
			if(args.endsWith("min")) {
				args = args.replace("min", "");
				int minutes = Integer.valueOf(args);
				if (minutes < 0) {
					minutes *= -1;
				}
				endTime = endTime + (minutes*60000);
			}
			if(args.endsWith("h")) {
				args = args.replace("h", "");
				int hours = Integer.valueOf(args);
				if (hours < 0) {
					hours *= -1;
				}
				endTime = endTime + (hours*3600000);
			}
		}
		if(endTime < 1000l) {
			endTime = 1000l;
		}
		return endTime;
	}
	
	@SuppressWarnings("deprecation")
	public static void sendErrorEmbled(TextChannel channel, String discription, Member m) {				
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" "+m.getUser().getAsMention()+" "+discription);
		builder.setColor(Melody.HEXEmbeldError);
		channel.sendMessage(builder.build()).queue();
	}
	
	public static void loadSystemData(Melody melody) {
		if(melody.getDatabase().isConnected()) {
			try {
				ResultSet rs = melody.getDatabase().onQuery("SELECT playedmusictime FROM system");
				if(rs.next()) {
					melody.playedmusictime = rs.getInt("playedmusictime");
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void saveSystemData(Melody melody) {
		if(melody.getDatabase().isConnected()) {
			try {
				ResultSet rs = melody.getDatabase().onQuery("SELECT playedmusictime FROM system");
				if(rs.next()) {	
					PreparedStatement ps = melody.getDatabase().getConnection().prepareStatement("UPDATE `system` SET `playedmusictime` = ?");
					ps.setLong(1, melody.playedmusictime);
					ps.executeUpdate();	
				}else {
					PreparedStatement ps = melody.getDatabase().getConnection().prepareStatement("INSERT INTO system (playedmusictime) VALUES (?)");
					ps.setLong(1, melody.playedmusictime);
					ps.executeUpdate();
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
package de.melody.tools;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.json.JSONObject;

import de.melody.core.Melody;

public class Utils {
	
	public static boolean doesGuildExist(Long GuildId) {
		try {
			ResultSet rs = Melody.INSTANCE._database.onQuery("SELECT guildid FROM guilds WHERE guildid = " + GuildId);
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
			ResultSet rs = Melody.INSTANCE._database.onQuery("SELECT COUNT(*) FROM userdata");
			if(rs.next()) {
				return rs.getLong("COUNT(*)");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return 0l;
	}
	
	public static Long getAllUsersHeardTimeSec() {
		try {
			ResultSet rs = Melody.INSTANCE._database.onQuery("SELECT SUM(heardtime) FROM userdata");
			if(rs.next()) {
				return rs.getLong("SUM(heardtime)");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return 0l;
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
				bool = true;
				break;
			case "false":
			case "off":
				bool = false;
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
	

	
	public static String getDomain(String url) {
		if(url.startsWith("http://") || url.startsWith("https://")) {
			String[] args = url.split("/");
			return args[2].toLowerCase();
		}
		return null;
	}
	
	public static JSONObject getJsonObject(File jsonfile) throws Exception {
		String content = new String(Files.readAllBytes(Paths.get(jsonfile.toURI())), "UTF-8");
		JSONObject json = new JSONObject(content);
		return json;
	}
	
	public static class IDGenerator{
		
		public static String generateToken() {
			String zufallstr1;
			String zufallstr2;
			String zufallstr3;
			String zufallstr4;
			
			int zufall1;
			int zufall2;
			int zufall3;
			int zufall4;
				
			Random r = new Random();
			zufall1 = r.nextInt(9999);
			zufall2 = r.nextInt(9999);
			zufall3 = r.nextInt(9999);
			zufall4 = r.nextInt(9999);
				
			zufallstr1 = zufall1+"";
			zufallstr2 = zufall2+"";
			zufallstr3 = zufall3+"";
			zufallstr4 = zufall4+"";
				
				
			if(zufall1 < 999) {
				if(zufall1 < 99) {
					if(zufall1 < 9) {
						zufallstr1 = "000"+zufall1;
					}else
						zufallstr1 = "00"+zufall1;
				}else
					zufallstr1 = "0"+zufall1;
			}
			if(zufall2 < 999) {
				if(zufall2 < 99) {
					if(zufall2 < 9) {
						zufallstr2 = "000"+zufall2;
					}else
						zufallstr2 = "00"+zufall2;
				}else
					zufallstr2 = "0"+zufall2;
			}
			if(zufall3 < 999) {
				if(zufall3 < 99) {
					if(zufall3 < 9) {
						zufallstr3 = "000"+zufall3;
					}else
						zufallstr3 = "00"+zufall3;
				}else
					zufallstr3 = "0"+zufall3;
			}
			if(zufall4 < 999) {
				if(zufall4 < 99) {
					if(zufall4 < 9) {
						zufallstr4 = "000"+zufall4;
					}else
						zufallstr4 = "00"+zufall4;
				}else
					zufallstr4 = "0"+zufall4;
			}
				String genID = zufallstr1+"-"+zufallstr2+"-"+zufallstr3+"-"+zufallstr4;
				return genID;
		}	
	
		public static String generateID() {
			char[] charakterlist = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
										'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
										'1','2','3','4','5','6','7','8','9','_','-'};
			String generatedid = "";
			Random random = new Random();
			for(int length = 0;length < 10;length++) {
				generatedid = generatedid + charakterlist[random.nextInt(charakterlist.length)];
			}
			return generatedid;
		}
	}
	
	public static class Emoji {
		
		public final static Long HEY_GUYS = 801638731136237568l;
		
		public final static String REFRESH = "🔄";
		public final static String NEXT_TITLE = "⏭";
		public final static String PREVIOUS_TITLE = "⏮";
		public final static String FAST_FORWARD = "⏩";
		public final static String REWIND = "⏪";
		public final static String PAUSE = "⏸";
		public final static String RESUME = "▶️";
		public final static String BACK = "◀️";
		public final static String STOP = "⏹";
		public final static String SINGLE_LOOP = "🔂";
		public final static String QUEUE_LOOP = "🔁";
		public final static String MUSIC_NOTE = "🎶";
		public final static String INFINITY = "∞";
		public final static String EXIT = "❌";
		public final static String SPARKLING_HEART = "💖";
		public final static String OK_HAND = "👌";
		public final static String EXCLAMATION_MARK = "❗️";
		public final static String CLIPBOARD = "📋";
		public final static String PENCIL = "✏️";
		public final static String CHECK_MARK = "✅";
		public final static String WHITE_FLAG = "🏳️";
		public final static String FIRECRACKER = "🧨";
		public final static String BELL = "🔔";
		public final static String GERMANY_FLAG = "🇩🇪";
		public final static String UNITED_STATES_FLAG = "🇺🇸";
	}

}


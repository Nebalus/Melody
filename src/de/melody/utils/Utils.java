package de.melody.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.json.JSONObject;

import de.melody.core.Constants;
import de.melody.core.Melody;

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
	
	public static Long getUserInt() {
		try {
			ResultSet rs = Melody.INSTANCE.getDatabase().onQuery("SELECT COUNT(*) FROM userdata");
			if(rs.next()) {
				return rs.getLong("COUNT(*)");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return 0l;
	}
	
	public static Long getAllUsersHeardTimeInt() {
		try {
			ResultSet rs = Melody.INSTANCE.getDatabase().onQuery("SELECT SUM(heardtime) FROM userdata");
			if(rs.next()) {
				return rs.getLong("SUM(heardtime)");
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
		
		String uptimeSuffix = "";
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
		return uptimeSuffix;
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
			try {
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
			}catch(NumberFormatException e) {}
		}
		if(endTime < 1000l) {
			endTime = 10000l;
		}
		return endTime;
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
	
	public static class ConsoleLogger {

	    final static SimpleDateFormat time = new SimpleDateFormat("<HH:mm:ss> ");
	   
	    //info
	    public static void info(Object className, Object message) {
	        System.out.println(time.format(new Date())+ "[Info] " + className + " : " + message);
	    }
	    
	    public static void info(Object message) {
	        System.out.println(time.format(new Date())+ "[Info] : " + message);
	    }

	    //error
	    public static void error(Object className, Object message) {
	        System.out.println(time.format(new Date()) + "[Error] " + className + " : " + message);
	    }
	    
	    public static void error( Object message) {
	        System.out.println(time.format(new Date()) + "[Error] : " + message);
	    }

	    //debug
	    public static void debug(Object className, Object message) {
	    	if(Constants.DEBUGMODE) {
	    		System.out.println(time.format(new Date()) + "[Debug] " + className + " : " + message);
	    	}
	    }
	    
	    public static void debug(Object message) {
	    	if(Constants.DEBUGMODE) {
	    		System.out.println(time.format(new Date()) + "[Debug] : " + message);
	    	}
	    }

	    //warning
	    public static void warning(Object className, Object message) {
	        System.out.println(time.format(new Date()) + "[Warning] " + className + " : " + message);
	    }
	    
	    public static void warning(Object message) {
	        System.out.println(time.format(new Date()) + "[Warning] : " + message);
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


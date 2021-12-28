package de.melody.tools.helper;

public class MathHelper {

	public static Long countupUntil(Long value, Long max) {
		if(value > max) {
			return max;
		}else {
			return value;
		}
	}
	
	public static Long countdownUntil(Long value, Long min) {
		if(value < min) {
			return min;
		}else {
			return value;
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
				if(args.endsWith("hour")) {
					args = args.replace("hour", "");
					int hours = Integer.valueOf(args);
					if (hours < 0) {
						hours *= -1;
					}
					endTime = endTime + (hours*3600000);
				}
			}catch(NumberFormatException e) {
				return -1l;
			}
		}
		return endTime;
	}
	
	public static String decodeStringFromTimeMillis(long time) {
		long uptime = time/1000;

		long sekunden = uptime;
		long minuten = sekunden/60;
		long stunden = minuten/60;
		long tage = stunden/24;
		stunden %= 24;
		minuten %= 60;
		sekunden %= 60;
		
		String uptimeSuffix = "";
		if(uptime == 0) {
			uptimeSuffix = "0sec";
		}
		if(sekunden > 0) {
			uptimeSuffix = sekunden +"sec";
		}
		if(minuten > 0) {
			uptimeSuffix = minuten +"min "+uptimeSuffix;
		}
		if(stunden > 0) {
			uptimeSuffix = stunden +"hour"+(stunden == 1 ? "s " : " ")+uptimeSuffix;
		}
		if(tage > 0) {
			uptimeSuffix = tage +"day"+(tage == 1 ? "s " : " ")+uptimeSuffix;
		}
		return uptimeSuffix;
	}
}

package de.nebalus.dcbots.melody.tools;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public final class Utils
{
	
	public static JSONObject getJsonObject(File jsonfile) throws Exception 
	{
		String content = new String(Files.readAllBytes(Paths.get(jsonfile.toURI())), "UTF-8");
		JSONObject json = new JSONObject(content);
		return json;
	}
	
	public static float getPercent(Long number, Long maxNumber)
	{
		return number * 100 / maxNumber;
	}
	
	public static float getPercent(int number, int maxNumber)
	{
		return number * 100 / maxNumber;
	}
	
	public static String decodeStringFromTimeMillis(long time) 
	{
		long uptime = time/1000;
		long seconds = uptime;
		long minutes = seconds/60;
		long hours = minutes/60;
		long days = hours/24;
		hours %= 24;
		minutes %= 60;
		seconds %= 60;
		
		String decodedtime = "";
		if(uptime == 0) 
		{
			decodedtime = time + "ms";
		}
		else 
		{
			if(seconds > 0) 
			{
				decodedtime = seconds + "sec";
			}
			
			if(minutes > 0) 
			{
				decodedtime = minutes + "min " + decodedtime;
			}
			
			if(hours > 0) 
			{
				decodedtime = hours + "hour" + (hours > 1 ? "s " : " ") + decodedtime;
			}
			
			if(days > 0) 
			{
				decodedtime = days + "day" + (days > 1 ? "s " : " ") + decodedtime;
			}
		}
		
		return decodedtime;
	}
}

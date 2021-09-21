package de.melody.core;

import java.awt.Color;
import java.util.List;

public class Config{
	
	public static boolean DEVMODE = false;
	public static boolean DEBUGMODE = false;
	
	public static final long STARTUP = System.currentTimeMillis();
	
	public static final String BUILDVERSION = "Beta 0.6.0";	
	public static final String BUILDDATE = "21/9/2021";	
	public static final String BUILDNAME = "Melody";
	
	public static final Long ENTITYEXPIRETIME = 1000l*60l*60l;
	
	public static final Color EMBELD_ERRORCOLOR = Color.RED;
	
	public static final int MAXQUEUELENGTH = 200;
	public static final int MAXPLAYERQUEUELENGTH = 100;
	
	public static final String INVITE_URL = "https://melodybot.ga/invite";
	public static final String WEBSITE_URL = "https://melodybot.ga/";
	
	public static final int MUSIK_AFK_DEFAULT = 60;
	
	public static final List<Long> DEVELOPERIDS = List.of(502213965485703168l);
}

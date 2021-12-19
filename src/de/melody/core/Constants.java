package de.melody.core;

import java.awt.Color;
import java.io.File;
import java.util.List;

public final class Constants{
	
	public static final boolean DEVMODE = false;
	public static final boolean DEBUGMODE = true;
	public static final boolean ALLOWSLASHCOMMANDS = true;
	
	public static final long STARTUP = System.currentTimeMillis();
	
	public static final String BUILDVERSION = "BETA PATCH 0.6.4c";	
	public static final String BUILDDATE = "2021-12-11";	
	public static final String BUILDNAME = "Melody";
	
	public static final String DEFAULTPREFIX = "m!";
	
	public static final Long ENTITYEXPIRETIME = 1000l*60l*60l;
	
	public static final Color EMBEDCOLOR = Color.decode("#2eb8bf");
	public static final Color ERROREMBEDCOLOR = Color.RED;
	
	public static final String ICON_URL = "https://melodybot.ga/assets/images/icon.png";
	public static final String INVITE_URL = "https://melodybot.ga/invite";
	public static final String COMMAND_URL = "https://melodybot.ga/commands";
	public static final String WEBSITE_URL = "https://melodybot.ga/";
	
	public static final int MUSIK_AFK_DEFAULT = 60;
	
	public static final List<Long> DEVELOPERIDS = List.of(502213965485703168l);
	
	public static final int MAXVOLUME = 100;
	public static final int MAXCLEANMESSAGES = 200;	
	public static final int DEFAULTCLEANMESSAGES = 100;	
	public static final int MAXQUEUELENGTH = 200;
	public static final int MAXPLAYEDQUEUELENGTH = 200;
	
	public static final File TEMP_DIRECTORY = new File(Melody.getCurrentJarPath()+"/temp/");
	
	public static final String PIC_DEFAULT_AVATAR_URL = "/default-avatar.png";
	public static final String PIC_ERROR_LINE_URL = "/error-line.png";
	public static final String PIC_TRACKINFO_URL = "/trackinfo.png";
	
	public static final String STORAGE_DATABASE_URL = "/database.db";
}

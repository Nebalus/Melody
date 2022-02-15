package de.melody.core;

import java.awt.Color;
import java.util.List;

public final class Constants{
	
	public static final String BUILDVERSION = "BETA 0.6.6";	
	public static final String BUILDDATE = "2021-01-22";	
	public static final String BUILDNAME = "Melody";
	
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
	
	public static final String PIC_DEFAULT_AVATAR_URL = "/default-avatar.png";
	public static final String PIC_ERROR_LINE_URL = "/error-line.png";
	public static final String PIC_TRACKINFO_URL = "/trackinfo.png";
}

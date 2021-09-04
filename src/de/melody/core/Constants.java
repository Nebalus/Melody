package de.melody.core;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;

public class Constants {
	
	public static boolean DEVMODE = false;
	
	public static final long STARTUP = System.currentTimeMillis();
	
	public static final String BUILDVERSION = "Beta 0.6.0";	
	public static final String BUILDDATE = "02/9/2021";	
	public static final String BUILDNAME = "Melody";
	
	public static final Long ENTITYEXPIRETIME = 1000l*60l*60l;
	
	public static final Color EMBEDCOLOR = Color.decode("#2eb8bf");
	public static final Color EMBELD_ERRORCOLOR = Color.RED;
	
	public static final EmbedBuilder EMBED_DEFAULT = new EmbedBuilder().setColor(EMBEDCOLOR);
	public static final EmbedBuilder EMBED_ERROR = new EmbedBuilder().setColor(EMBELD_ERRORCOLOR);
	
	public static final int MAXQUEUELENGTH = 200;
	public static final int MAXPLAYERQUEUELENGTH = 100;
	
	public static final String INVITE_URL = "https://nebalus.ga/melody/invite";
	public static final String WEBSITE_URL = "https://nebalus.ga/melody/";
	
	public static final int MUSIK_AFK_DEFAULT = 60;
	
	public static final List<Long> DEVELOPERIDS = List.of(502213965485703168l);
}

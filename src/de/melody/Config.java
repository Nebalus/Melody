package de.melody;

import java.awt.Color;
import java.util.List;

public class Config {
	public static final String buildversion = "Beta 0.5.8";	
	public static final String builddate = "18/8/2021";	
	public static final String buildname = "Melody";
	
	public static final Long entityexpiretime = 1000l*60l*60l;
	public static final Color HEXEmbeld = Color.decode("#2eb8bf");
	public static final Color HEXEmbeldError = Color.RED;
	
	public static final int maxqueuelength = 200;
	public static final int maxplayedqueuelength = 100;
	
	public static final String invitelink = "https://nebalus.ga/melody/invite";
	public static final String homepagelink = "https://nebalus.ga/melody/";
	
	public static final int music_afk_default = 60;
	
	public static final List<Long> developerids = List.of(502213965485703168l);
}

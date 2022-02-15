package de.melody.tools.entitymanager.entitys;

import de.melody.core.Melody;
import de.melody.tools.datamanager.files.LiteSQL;
import de.melody.tools.messenger.Language;

public final class GuildEntity {

	private Long guildid;
	
	// Options
	private int volume = 50;
	private String prefix = Melody.INSTANCE.getConfig()._defaultprefix;
	private int playtime = 0;
	private Long lastaudiochannelid = 0l;
	private boolean djonly = false;
	private boolean voteskip = false;
	private boolean staymode = false;
	private boolean announcesongs = true;
	private boolean preventduplicates = false;
	private Language language = Language.ENGLISH;
	
	private Melody melody = Melody.INSTANCE;
	private LiteSQL database = melody.getDatabase();
	
	public GuildEntity(Long guildid) {
		this.guildid = guildid;
		if(database.isConnected()) {
			
		}
	}
	
	public Language getLanguage() {
		return language;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	/*
	public enum GuildOptions{
		VOLUME("volume"),
		PREFIX("prefix"),
		LASTAUDIOCHANNEL("lastaudiochannel"),
		PLAYTIME("playtime");
		
		final String databasename;
		
		GuildOptions(String databasename){
			this.databasename = databasename; 
		}
	
		public String getDatabaseName() {
			return databasename;
		}
	}
	*/
}

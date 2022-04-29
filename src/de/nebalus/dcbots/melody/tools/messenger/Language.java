package de.nebalus.dcbots.melody.tools.messenger;

import de.nebalus.dcbots.melody.tools.datamanager.FileResource;

public enum Language {

	ENGLISH("English", "US", 0, FileResource.LANG_ENGLISH),
	GERMAN("Deutsch", "DE", 1, FileResource.LANG_GERMAN);
	
	final String name;
	final String code;
	final int databaseid;
	final FileResource resource;
	
	private Language(String name, String code, int databaseid, FileResource resource) {
		this.name = name;
		this.code = code;	
		this.databaseid = databaseid;
		this.resource = resource;
	}
	
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public int getDatabaseID() {
		return databaseid;
	}
	
	public String getFilePath() {
		return resource.getFilePath();
	}
	
	public static Language getLanguage(String code) {
		 for (Language language : values()) {
			 if(language.code.equalsIgnoreCase(code) || language.name.equalsIgnoreCase(code)) {
				 return language;
			 }
         }
		return ENGLISH;
	}
	
	public static Language getLanguage(int databaseid) {
		 for (Language language : values()) {
			 if(language.databaseid == databaseid) {
				 return language;
			 }
        }
		return ENGLISH;
	}
}

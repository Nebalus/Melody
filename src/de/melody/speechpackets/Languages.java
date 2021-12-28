package de.melody.speechpackets;

import de.melody.tools.Utils.Emoji;

public enum Languages {

	ENGLISH("English","English.json","US",Emoji.UNITED_STATES_FLAG,0),
	GERMAN("Deutsch","German.json","DE",Emoji.GERMANY_FLAG,1);
	
	String name;
	String file;
	String code;
	String icon;
	int databaseid;
	
	private Languages(String name,String file,String code,String icon,int databaseid) {
		this.name = name;
		this.file = file;
		this.code = code;	
		this.icon = icon;	
		this.databaseid = databaseid;
	}
	
	public String getFileName() {
		return file;
	}
	public String getCode() {
		return code;
	}
	public String getIcon() {
		return icon;
	}
	public String getName() {
		return name;
	}
	public int getDatabaseID() {
		return databaseid;
	}
	
	public static Languages getLanguage(String code) {
		 for (Languages language : values()) {
			 if(language.code.equalsIgnoreCase(code) || language.name.equalsIgnoreCase(code)) {
				 return language;
			 }
         }
		return ENGLISH;
	}
	
	public static Languages getLanguage(int id) {
		 for (Languages language : values()) {
			 if(language.databaseid == id) {
				 return language;
			 }
        }
		return ENGLISH;
	}
}

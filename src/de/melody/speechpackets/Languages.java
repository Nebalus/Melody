package de.melody.speechpackets;

import de.melody.utils.Emojis;

public enum Languages {

	ENGLISH("English","English.json","US",Emojis.UNITED_STATES_FLAG),
	GERMAN("Deutsch","German.json","DE",Emojis.GERMANY_FLAG);
	
	String name;
	String file;
	String code;
	String icon;

	private Languages(String name,String file,String code,String icon) {
		this.name = name;
		this.file = file;
		this.code = code;	
		this.icon = icon;	
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
	
	public static Languages getLanguage(String code) {
		 for (Languages language : values()) {
			 if(language.code.equalsIgnoreCase(code) || language.name.equalsIgnoreCase(code)) {
				 return language;
			 }
         }
		return ENGLISH;
	}
}

package de.pixelbeat.speechpackets;

public enum Languages {

	ENGLISH("English","English.json","US","🇺🇸"),
	GERMAN("Deutsch","German.json","DE","🇩🇪");
	
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
	
	public static Languages getLanguage(String code) {
		 for (Languages language : values()) {
			 if(language.code.equalsIgnoreCase(code) || language.name.equalsIgnoreCase(code)) {
				 return language;
			 }
         }
		return ENGLISH;
	}
}

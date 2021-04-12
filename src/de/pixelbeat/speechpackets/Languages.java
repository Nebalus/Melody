package de.pixelbeat.speechpackets;

public enum Languages {

	ENGLISH("English","English.json","EN"),
	GERMAN("Deutsch", "German.json", "DE");
	
	String name;
	String file;
	String code;

	private Languages(String name,String file,String code) {
		this.name = name;
		this.file = file;
		this.code = code;	
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

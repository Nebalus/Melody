package de.melody.music;

public enum LoopMode {
	QUEUE("queue"),
	SONG("song"),
	NONE("off");
	String textformat;
	
	LoopMode(String textformat) {
		this.textformat = textformat;
	}
	
	public String getTextFormat() {
		return textformat;
	}
	
	public static LoopMode getFromString(String input) {
		for(LoopMode lm : LoopMode.values()) {
			if(lm.getTextFormat().equalsIgnoreCase(input)) {
				return lm;
			}
		}
		return null;
	}
}

package de.melody.tools.commandbuilder;

public enum CommandType{
	CHAT,
	SLASH,
	BOTH;
	
	public boolean isChat(){
		if(this == CHAT || this == BOTH) {
			return true;
		}
		return false;
	}
	
	public boolean isSlash(){
		if(this == SLASH || this == BOTH) {
			return true;
		}
		return false;
	}
}
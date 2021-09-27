package de.melody.utils.commandbuilder;

public enum CommandType{
	CHAT_COMMAND,
	SLASH_COMMAND,
	BOTH;
	
	public boolean isChat(){
		if(this == CHAT_COMMAND || this == BOTH) {
			return true;
		}
		return false;
	}
	
	public boolean isSlash(){
		if(this == SLASH_COMMAND || this == BOTH) {
			return true;
		}
		return false;
	}
}
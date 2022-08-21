package de.nebalus.dcbots.melody.tools.audioplayer.enums;

public enum LoopMode 
{
	QUEUE("queue"),
	SONG("song"),
	NONE("off");
	
	final String textformat;
	
	LoopMode(String textformat) 
	{
		this.textformat = textformat;
	}
	
	public String getTextFormat() 
	{
		return textformat;
	}
	
	public static LoopMode getFromString(String input)
	{
		for(LoopMode lm : LoopMode.values()) 
		{
			if(lm.getTextFormat().equalsIgnoreCase(input))
			{
				return lm;
			}
		}
		return null;
	}
}

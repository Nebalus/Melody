package de.nebalus.dcbots.melody.tools.messenger;

import java.awt.Color;
import java.io.File;

import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.tools.datamanager.FileResource;
import de.nebalus.dcbots.melody.tools.datamanager.ImageGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ReworkMessageBuilder 
{
	private String HEADER_TEXT;
	private String BODY_TEXT;
	private String FOOTER_TEXT;
	
	private final String PREFIX_FOOTER_TEXT = "· "+Build.VERSION+" · ";
	
	private Color EMBED_COLOR = new Color(47, 49, 54);
	
	private File COLORFILE = FileResource.IMG_COLORLINE.getFile();
	private boolean ISCOLORLINEENABLED = true;
	
	public ReworkMessageBuilder() {}
	
	public ReworkMessageBuilder setHeader(String header) 
	{
		HEADER_TEXT = header;
		return this;
	}
	
	public ReworkMessageBuilder setBody(String body) 
	{
		BODY_TEXT = body;
		return this;
	}
	
	public ReworkMessageBuilder setFooter(String footer) 
	{
		FOOTER_TEXT = footer;
		return this;
	}
	
	public ReworkMessageBuilder setColor(Color color)
	{
		EMBED_COLOR = color;
		return this;
	}
	
	public ReworkMessageBuilder enableColorLine()
	{
		ISCOLORLINEENABLED = true;
		return this;
	}

	public ReworkMessageBuilder disableColorLine()
	{
		ISCOLORLINEENABLED = false;
		return this;
	}
	
	public boolean isColorLineEnabled()
	{
		return ISCOLORLINEENABLED;
	}
	
	public File getImageFile()
	{
		return COLORFILE;  
	}

	public MessageEmbed build() 
	{
		EmbedBuilder builder = new EmbedBuilder();
		
		if(HEADER_TEXT != null) 
		{
			HEADER_TEXT = "> **" +HEADER_TEXT + "**";
			builder.setTitle(HEADER_TEXT);
		}
		
		if(FOOTER_TEXT != null) 
		{
			builder.setFooter(PREFIX_FOOTER_TEXT + FOOTER_TEXT);
		}
		else 
		{
			builder.setFooter(PREFIX_FOOTER_TEXT);
		}
		
		if(ISCOLORLINEENABLED)
		{
			COLORFILE = ImageGenerator.generateColorLine(EMBED_COLOR);
			builder.setImage("attachment://colorline.png");
		}
		
		builder.setDescription(BODY_TEXT);
		builder.setColor(EMBED_COLOR);
		
		return builder.build();
	}

}

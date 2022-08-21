package de.nebalus.dcbots.melody.tools.messenger.embedbuilders;

import java.awt.Color;
import java.io.File;
import javax.annotation.Nullable;

import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.tools.datamanager.FileResource;
import de.nebalus.dcbots.melody.tools.datamanager.ImageGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ColorLineEmbedBuilder
{
	private final String PREFIX_FOOTER_TEXT = "· "+Build.VERSION+" · ";
	
	private String HEADER_TEXT;
	private String BODY_TEXT;
	private String FOOTER_TEXT;
	private String THUMBNAIL_URL;
	private Color EMBED_COLOR = new Color(47, 49, 54);
	private File COLORFILE = FileResource.IMG_COLORLINE.getFile();
	private boolean ISCOLORLINEENABLED = true;
	
	private EmbedBuilder EMBEDBUILDER = new EmbedBuilder(); 
	
	public ColorLineEmbedBuilder() {}
	
	public ColorLineEmbedBuilder setHeader(@Nullable String header) 
	{
		HEADER_TEXT = header;
		return this;
	}
	
	public ColorLineEmbedBuilder setBody(@Nullable String body) 
	{
		BODY_TEXT = body;
		return this;
	}
	
	public ColorLineEmbedBuilder setFooter(@Nullable String footer) 
	{
		FOOTER_TEXT = footer;
		return this;
	}
	
	public ColorLineEmbedBuilder setThumbnail(@Nullable String url) 
	{
		THUMBNAIL_URL = url;
		return this;
	}
	
	public ColorLineEmbedBuilder setColor(@Nullable Color color)
	{
		EMBED_COLOR = color;
		return this;
	}
	
	public ColorLineEmbedBuilder enableColorLine()
	{
		ISCOLORLINEENABLED = true;
		return this;
	}

	public ColorLineEmbedBuilder disableColorLine()
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
		if(HEADER_TEXT != null) 
		{
			EMBEDBUILDER.setTitle("> **" + HEADER_TEXT + "**");
		}
		
		if(FOOTER_TEXT != null) 
		{
			EMBEDBUILDER.setFooter(PREFIX_FOOTER_TEXT + FOOTER_TEXT);
		}
		else 
		{
			EMBEDBUILDER.setFooter(PREFIX_FOOTER_TEXT);
		}
		
		if(ISCOLORLINEENABLED)
		{
			COLORFILE = ImageGenerator.generateColorLine(EMBED_COLOR);
			EMBEDBUILDER.setImage("attachment://colorline.png");
		}
		
		if(BODY_TEXT != null)
		{
			EMBEDBUILDER.setDescription(BODY_TEXT);
		}
		
		EMBEDBUILDER.setThumbnail(THUMBNAIL_URL);
		EMBEDBUILDER.setColor(EMBED_COLOR);
		
		return EMBEDBUILDER.build();
	}
}

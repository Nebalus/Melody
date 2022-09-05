package de.nebalus.dcbots.melody.tools.messenger.embedbuilders;

import java.awt.Color;
import java.io.File;

import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.tools.datamanager.ImageGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class RemasteredEmbedData 
{
	private final String PREFIX_FOOTER_TEXT = "· " + Build.VERSION + " · ";
	
	private final String HEADER_TEXT;
	private final String BODY_TEXT;
	private final String FOOTER_TEXT;
	private final String THUMBNAIL_URL;
	private final Color COLOR_SCHEME;
	private final File COLORLINEFILE;
	private final boolean ISCOLORLINEENABLED;
	private final MessageEmbed MESSAGEEMBED; 
	
	public RemasteredEmbedData(String header, String body, String footer, String thumburl, Color colorscheme, boolean iscolorlineenabled)
	{
		HEADER_TEXT = header;
		BODY_TEXT = body;
		FOOTER_TEXT = footer;
		THUMBNAIL_URL = thumburl;
		COLOR_SCHEME = colorscheme;
		ISCOLORLINEENABLED = iscolorlineenabled;
		
		final EmbedBuilder builder = new EmbedBuilder();
		
		if(HEADER_TEXT != null) 
		{
			builder.setTitle("> **" + HEADER_TEXT + "**");
		}
		
		if(BODY_TEXT != null)
		{
			builder.setDescription(BODY_TEXT);
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
			COLORLINEFILE = ImageGenerator.generateColorLine(COLOR_SCHEME);
			builder.setImage("attachment://colorline.png");
		}
		else
		{
			COLORLINEFILE = null;
		}
		
		if(THUMBNAIL_URL != null)
		{
			builder.setThumbnail(THUMBNAIL_URL);
		}
		
		builder.setColor(COLOR_SCHEME);
		
		MESSAGEEMBED = builder.build();
	}
	
	public String getHeader()
	{
		return HEADER_TEXT;
	}
	
	public String getBody()
	{
		return BODY_TEXT;
	}
	
	public String getFooter()
	{
		return FOOTER_TEXT;
	}
	
	public String getThumbnail()
	{
		return THUMBNAIL_URL;
	}
	
	public Color getColorScheme()
	{
		return COLOR_SCHEME;
	}
	
	public boolean isColorLineEnabled()
	{
		return ISCOLORLINEENABLED;
	}
	
	public File getColorLineFile()
	{
		return COLORLINEFILE;  
	}
	
	public MessageEmbed getAsMessageEmbed()
	{
		return MESSAGEEMBED;
	}
	
	public MessageCreateData getAsMessageCreateData()
	{
		final MessageCreateBuilder mcb = new MessageCreateBuilder();
		
		mcb.addEmbeds(MESSAGEEMBED);
		
		if(ISCOLORLINEENABLED && COLORLINEFILE.exists() && COLORLINEFILE.canRead())
		{
			mcb.setFiles(FileUpload.fromData(COLORLINEFILE, "colorline.png"));
		}
		
		return mcb.build();
	}
	
}

package de.nebalus.dcbots.melody.tools.datamanager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.nebalus.dcbots.melody.core.Melody;

public class ImageGenerator 
{
	
	public static File generateColorLine(Color color)
	{
		try 
		{
			final String filename = String.format("#%02X%02X%02X.png", color.getRed(), color.getGreen(), color.getBlue());
			final File colorlinefile = new File(Melody.getDataManager().getTempDirectory().getPath() + "/colorlines/" + filename);
			
			if(!colorlinefile.exists())
			{
				BufferedImage buffcolorline = ImageIO.read(FileResource.IMG_COLORLINE.getFile());
				
				final int width = buffcolorline.getWidth();
				final int height = buffcolorline.getHeight();
				
				BufferedImage buffimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				
				Graphics2D graph = buffimg.createGraphics();
				
				graph.setColor(color);
				graph.fillRect(0, 0, width, height);
				
				graph.dispose();
				
		
				colorlinefile.mkdirs();
				
				colorlinefile.createNewFile();
				
				ImageIO.write(buffimg, "png", colorlinefile);
				return colorlinefile;
			}
			else
			{
				return colorlinefile;
			}	
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return FileResource.IMG_COLORLINE.getFile();
		}
	}
	
}

package de.melody.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.datamanagment.GenerateFile;
import de.melody.speechpackets.MessageFormatter;
import de.melody.tools.Utils.Emoji;
import de.melody.tools.helper.MathHelper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class Images {

	private static MessageFormatter mf = Melody.INSTANCE._messageformatter;
	
	public static File tracktopng(String trackname,long trackplaytime, long tracklength, String trackauthor, Guild guild, Member userqueued) {
		try {
			BufferedImage background = ImageIO.read((Images.class.getResource(Constants.PIC_TRACKINFO_URL)));
			
			final int width = background.getWidth();
			final int height = background.getHeight();
			
			BufferedImage buffimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			Graphics2D graph = buffimg.createGraphics();
			graph.drawImage(background, null, 0, 0);
			
			
			final int barXPos = 98;
			final int barYPos = 199;
			final int barLength = 930;
			final int barHeight = 15;
			final int barloadframe = barLength/100;
			
			//Empty bar
			graph.setColor(Color.decode("#6A6A6D"));
			graph.fillRoundRect(barXPos, barYPos, barLength, barHeight, barHeight, barHeight);
			
			//Loaded bar
			graph.setColor(Color.RED);
			graph.fillRoundRect(barXPos, barYPos, barloadframe*getPercent(trackplaytime,tracklength), barHeight, barHeight, barHeight);
			
			//Main text
			graph.setColor(Color.WHITE);
			graph.setFont(new Font("SansSerif",Font.PLAIN, 30));
			
			final int maxcharacter = 42;
			String currenttrackname = "";
			if(trackname != null) {
				int currentcharacters = 0;
				for(int i = 0; i < trackname.length(); i++) {    
					if(currentcharacters > maxcharacter) {
						if(!currenttrackname.endsWith("...")) currenttrackname = currenttrackname +"...";
					}else {
						currenttrackname = currenttrackname+ trackname.charAt(i);
						currentcharacters++;
					}
			    }    
			}else {	
				currenttrackname = mf.format(guild,"music.track.playing-nothing");
				}
			graph.drawString(mf.format(guild, "music.track.currently-playing")+currenttrackname, 69, 90);
			
			//Author text
			graph.setFont(new Font("SansSerif",Font.PLAIN, 23));
			String currenttrackauthor;
			if(trackauthor != null) {
				currenttrackauthor = trackauthor;
			}else {
				currenttrackauthor = mf.format(guild, "music.track.author-null");
			}
			graph.drawString(mf.format(guild, "music.track.author",currenttrackauthor), 119, 160);
		
			//Playing text
			graph.setFont(new Font("SansSerif",Font.PLAIN, 20));	
			
			String currenttrackplaytime = MathHelper.getTimeFormat(trackplaytime);
			if(currenttrackplaytime == null) {
				currenttrackplaytime = "N/ A";
			}
			
			graph.drawString(currenttrackplaytime, 119,250);
			//Ending text
			
			String currenttracklength = MathHelper.getTimeFormat(tracklength);
			if(currenttracklength == null) {
				currenttracklength = "INFINITE "+Emoji.INFINITY;
			}
			graph.drawString(currenttracklength, 960,250);
			
			//Requested by text
			graph.setFont(new Font("SansSerif",Font.PLAIN, 23));
			graph.drawString(mf.format(guild, "music.user.who-requested"), 25,height-95);
			
			graph.setFont(new Font("SansSerif",Font.PLAIN, 36));
			
			if(userqueued != null) {
				InputStream avatar;
				try {
					avatar = new URL(userqueued.getUser().getAvatarUrl()).openStream();
				}catch (Exception e) {
					avatar = new URL(userqueued.getUser().getDefaultAvatarUrl()).openStream();
				}
				BufferedImage buffavatar = ImageIO.read(avatar);
				graph.drawImage(buffavatar, 50, height-75, 50, 50, null);
				graph.drawString(userqueued.getUser().getAsTag(), 112, height-35);
			}else {
				BufferedImage buffavatar = ImageIO.read((Images.class.getResource(Constants.PIC_DEFAULT_AVATAR_URL)));
				graph.drawImage(buffavatar, 50, height-75, 50, 50, null);	
				graph.drawString("Nobody#0042", 112, height-35);
			}		
			graph.dispose();	
			return generateNewImage(buffimg);
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static File generateNewImage(BufferedImage image) throws IOException {
		File file = new GenerateFile("png").getFile();
		ImageIO.write(image, "png", file);
		return file;
	}
	
	private static int getPercent(Long number, Long maxNumber){
		int percent = 0;
		try {
			percent = (int) (number*100/maxNumber);
		}catch (ArithmeticException e) {}
		return percent;
	}
}

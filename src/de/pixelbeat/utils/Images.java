package de.pixelbeat.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.pixelbeat.PixelBeat;

public class Images {

	public static File tracktojpg(String trackname,long trackplaytime, long tracklength, String trackauthor) {
		try {
			BufferedImage background = ImageIO.read((Images.class.getResource("/trackinfo.png")));
			
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
			
			final int maxcharacter = 38;
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
				currenttrackname = "Nothing playing...";
				}
			graph.drawString("Currenty playing: "+currenttrackname, 69, 90);
			
			//Author text
			graph.setFont(new Font("SansSerif",Font.PLAIN, 23));
			String currenttrackauthor;
			if(trackauthor != null) {
				currenttrackauthor = trackauthor;
			}else {
				currenttrackauthor = "Nobody";
			}
			graph.drawString("Author: "+currenttrackauthor, 119, 160);
		
			//Playing text
			graph.setFont(new Font("SansSerif",Font.PLAIN, 20));
			
			
			
			String currenttrackplaytime = getTimeFormat(trackplaytime);
			if(currenttrackplaytime == null) {
				currenttrackplaytime = "N/ A";
			}
			
			graph.drawString(currenttrackplaytime, 119,250);
			//Ending text
			
			String currenttracklength = getTimeFormat(tracklength);
			if(currenttracklength == null) {
				currenttracklength = "INFINITE ∞";
			}
			graph.drawString(currenttracklength, 960,250);
			
			graph.dispose();
			
			
			File images = new File(PixelBeat.INSTANCE.getCurrentJarPath()+"/images/");
			if(!images.exists()) {
				images.mkdirs();
			}
			
			File newfile = new File(PixelBeat.INSTANCE.getCurrentJarPath()+"/images/"+ID_Manager.generateID()+".png");
			newfile.createNewFile();
			ImageIO.write(buffimg, "png", newfile);
			return newfile;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getTimeFormat(Long time) {
		
		if(time >= 1000) {
			long sekunden = time/1000;
			long minuten = sekunden/60;
			long stunden = minuten/60;
			sekunden %= 60;
			minuten %= 60;
			
			String timeformat = "";
			
			if(stunden > 0) {
				if(stunden <= 9) {
					timeformat = timeformat + "0"+stunden+":";
				}else timeformat = timeformat + stunden+":";	
			}
			
			if(minuten <= 9) {
				timeformat = timeformat + "0"+minuten+":";
			}else timeformat = timeformat + minuten+":";	
			
			if(sekunden <= 9) {
				timeformat = timeformat + "0"+sekunden;
			}else timeformat = timeformat + sekunden;
			
			return timeformat;
		}
		
		return null;
	}
	
	private static int getPercent(Long number, Long maxNumber){
		int percent = 0;
		try {
			percent = (int) (number*100/maxNumber);
		}catch (ArithmeticException e) {}
		return percent;
	}
}

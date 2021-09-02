package de.melody.utils;

import java.util.Random;

public class ID_Manager{
		
	public static String generateToken() {
		String zufallstr1;
		String zufallstr2;
		String zufallstr3;
		String zufallstr4;
		
		int zufall1;
		int zufall2;
		int zufall3;
		int zufall4;
		
		Random r = new Random();
		zufall1 = r.nextInt(9999);
		zufall2 = r.nextInt(9999);
		zufall3 = r.nextInt(9999);
		zufall4 = r.nextInt(9999);
		
		zufallstr1 = zufall1+"";
		zufallstr2 = zufall2+"";
		zufallstr3 = zufall3+"";
		zufallstr4 = zufall4+"";
		
		
		if(zufall1 < 999) {
			if(zufall1 < 99) {
				if(zufall1 < 9) {
					zufallstr1 = "000"+zufall1;
				}else
					zufallstr1 = "00"+zufall1;
			    }else
			    	zufallstr1 = "0"+zufall1;
		}
		if(zufall2 < 999) {
			if(zufall2 < 99) {
				if(zufall2 < 9) {
					zufallstr2 = "000"+zufall2;
				}else
					zufallstr2 = "00"+zufall2;
				}else
					zufallstr2 = "0"+zufall2;
		}
		if(zufall3 < 999) {
			if(zufall3 < 99) {
				if(zufall3 < 9) {
					zufallstr3 = "000"+zufall3;
				}else
					zufallstr3 = "00"+zufall3;
				}else
					zufallstr3 = "0"+zufall3;
		}
		if(zufall4 < 999) {
			if(zufall4 < 99) {
				if(zufall4 < 9) {
					zufallstr4 = "000"+zufall4;
				}else
					zufallstr4 = "00"+zufall4;
				}else
					zufallstr4 = "0"+zufall4;
		}
		String genID = zufallstr1+"-"+zufallstr2+"-"+zufallstr3+"-"+zufallstr4;
		return genID;
	}	

	public static String generateID() {
		char[] charakterlist = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
								'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
								'1','2','3','4','5','6','7','8','9','_','-'};
		String generatedid = "";
		Random random = new Random();
		for(int length = 0;length < 10;length++) {
			generatedid = generatedid + charakterlist[random.nextInt(charakterlist.length)];
		}
		return generatedid;
	}
}

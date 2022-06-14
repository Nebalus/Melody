package de.nebalus.dcbots.melody.tools.datamanager;

import java.util.Random;

public class IDGenerator
{
	
	public static String generateToken() 
	{
		String genID = "";
		final Random r = new Random();
		final int rounds = 4;
		
		for(int i = 1; i <= rounds; i++)
		{
			int currentnumb = r.nextInt(9999);
			if(currentnumb < 999) 
			{
				if(currentnumb < 99) 
				{
					if(currentnumb < 9) 
					{
						genID = genID + "000" + currentnumb;
					}
					else 
					{
						genID = genID + "00" + currentnumb;
					}
				}
				else
				{
					genID = genID + "0" + currentnumb;
				}
			}
			if(i < rounds)
			{
				genID = genID + "-";
			}
		}
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

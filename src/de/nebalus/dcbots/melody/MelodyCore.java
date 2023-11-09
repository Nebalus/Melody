package de.nebalus.dcbots.melody;

import de.nebalus.framework.gfw.api.GFW;

public class MelodyCore {

	private static MelodyApp melodyApp;
	
	public static void main(String[] args) {
		// Generates the Melody Application
		melodyApp = MelodyAppFactory.build(args);

		try {
			melodyApp.go();
		} catch (Exception e) {
			e.printStackTrace();
			
			GFW gfw = melodyApp.getGFW();
			
			if (gfw != null && gfw.isInitialized()) {
				gfw.getLogger().logError(e);
			}
			
			Runtime.getRuntime().exit(-1);
		}
	}
	
	public static MelodyApp getMelodyApp() {
		return melodyApp;
	}
}

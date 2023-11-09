package de.nebalus.dcbots.melody;

import de.nebalus.framework.gfw.api.GFW;

public class MelodyCore {

	public static void main(String[] args) {
		// Generates the Melody Application
		MelodyApp melodyApp = MelodyAppFactory.build(args);

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
}

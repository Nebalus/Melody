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

			GFW gFW = melodyApp.getGFW();

			if (gFW != null && gFW.isInitialized()) {
				gFW.getLogger().logError(e);
			}

			Runtime.getRuntime().exit(-1);
		}
	}

	public static MelodyApp getMelodyApp() {
		return melodyApp;
	}
}

package de.nebalus.dcbots.melody;

import de.nebalus.framework.gfw.api.GFW;

public class MelodyCore {

	private static MelodyApp melodyApp;

	public static void main(String[] args) {
		try {
			// Generates the Melody Application
			melodyApp = MelodyAppFactory.build(args);
			melodyApp.go();
		} catch (Exception e) {
			e.printStackTrace();

			if (melodyApp != null) {
				GFW gfw = melodyApp.getGFW();
				gfw.getLogger().logError(e);
			}

			Runtime.getRuntime().exit(-1);
		}
	}

	public static MelodyApp getMelodyApp() {
		return melodyApp;
	}
}

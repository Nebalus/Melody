package de.nebalus.dcbots.melody;

import java.io.File;

import de.nebalus.framework.gfw.api.GFW;
import de.nebalus.framework.gfw.api.GFWBuilder;
import de.nebalus.framework.gfw.api.GFWBuilder.GFWBuildData;
import de.nebalus.framework.gfw.modules.dcbot.DCBotModule;

public class MelodyAppFactory {

	public static MelodyApp build(String[] args) {
		// Loads and predefines some stuff for the GFW
		GFWBuilder gfwBuilder = new GFWBuilder();
		gfwBuilder.setStartArguments(args);
		gfwBuilder.setModulesToInit(DCBotModule.class);
		gfwBuilder.showTimestampInConsole(true);
		gfwBuilder.showColouredTextInConsole(true);
		gfwBuilder.setExecutionDirectory(new File(MelodyApp.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsoluteFile().getParentFile());
		GFWBuildData gfwBuildData = gfwBuilder.build();

		// Generates a new GFW instance with the with the build data
		GFW gfwInstance = new GFW(gfwBuildData);
		gfwInstance.initialize();

		return new MelodyApp(gfwInstance);
	}
}

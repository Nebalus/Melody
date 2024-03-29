package de.nebalus.dcbots.melody;

import java.io.File;

import de.nebalus.framework.gfw.api.service.logging.LogService;
import de.nebalus.framework.gfw.api.service.logging.Logger;
import de.nebalus.framework.gfw.api.GFWBuilder;
import de.nebalus.framework.gfw.api.GFW;
import de.nebalus.framework.gfw.modules.dcbot.DCBotModule;

public class MelodyAppFactory {

	@SuppressWarnings("unchecked")
	public static MelodyApp build(String[] args) throws Exception {
		// Loads and predefines some stuff for the GFW
		GFWBuilder gfwBuilder = new GFWBuilder();
		gfwBuilder.setStartArguments(args);
		gfwBuilder.setModulesToInit(DCBotModule.class);
		gfwBuilder.showTimestampInConsole(true);
		gfwBuilder.showColouredTextInConsole(true);
		gfwBuilder.allowCommandExecutionInConsole(true);
		gfwBuilder.setExecutionDirectory(new File(MelodyApp.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsoluteFile().getParentFile());

		// Generates a new GFW instance with the with the build data
		GFW gfw = gfwBuilder.build();
		gfw.initialize();

		LogService logService = gfw.getLogService();
		Logger appLogger = logService.buildLogger(MelodyApp.class.getSimpleName(), true);

		return new MelodyApp(gfw, appLogger);
	}
}

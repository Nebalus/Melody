package de.nebalus.dcbots.melody;

import de.nebalus.framework.gfw.api.GFW;
import de.nebalus.framework.gfw.api.service.file.FileService;
import de.nebalus.framework.gfw.api.service.logging.LogService;
import de.nebalus.framework.gfw.api.service.logging.Logger;
import de.nebalus.framework.gfw.api.service.module.ModuleService;
import de.nebalus.framework.gfw.modules.dcbot.DCBotModule;

public class MelodyApp {

	public MelodyApp() {
	}

	protected void go() throws Exception {
		// Gets some essential services from the GFW instance
		FileService fileService = GFW.getFileService();
		LogService logService = GFW.getLogService();
		ModuleService moduleService = GFW.getModuleService();

		// Gets the own logger
		Logger appLogger = logService.buildLogger(getClass().getSimpleName(), true);

		// Gets the DCBotModule
		DCBotModule dcbot = (DCBotModule) moduleService.getModule(DCBotModule.class);

		// Creates an instance from the MelodyBot
		MelodyBotInstance botInstance = new MelodyBotInstance();
		
		dcbot.loadBotInstance("melody", botInstance);
	}
}

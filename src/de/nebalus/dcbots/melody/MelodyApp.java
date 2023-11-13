package de.nebalus.dcbots.melody;

import de.nebalus.framework.gfw.api.GFW;
import de.nebalus.framework.gfw.api.service.file.FileService;
import de.nebalus.framework.gfw.api.service.logging.LogService;
import de.nebalus.framework.gfw.api.service.logging.Logger;
import de.nebalus.framework.gfw.api.service.module.ModuleService;
import de.nebalus.framework.gfw.modules.dcbot.DCBotModule;

public class MelodyApp {

	private Logger appLogger;

	public MelodyApp() {
	}

	protected void go() throws Exception {
		// Gets some essential services from the GFW instance
		FileService fileService = GFW.getFileService();
		LogService logService = GFW.getLogService();
		ModuleService moduleService = GFW.getModuleService();

		// Gets the own logger
		appLogger = logService.buildLogger(getClass().getSimpleName(), true);

		// Gets the DCBotModule
		DCBotModule dcbot = (DCBotModule) moduleService.getModule(DCBotModule.class);

		// Creates an instance from the MelodyBot
		MelodyBotInstance botInstance = new MelodyBotInstance(this);
		dcbot.loadBotInstance("melody", botInstance);
	}

	public Logger getLogger() {
		return appLogger;
	}
}
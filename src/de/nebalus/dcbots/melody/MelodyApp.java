package de.nebalus.dcbots.melody;

import de.nebalus.framework.gfw.api.GFW;
import de.nebalus.framework.gfw.api.service.file.FileService;
import de.nebalus.framework.gfw.api.service.logging.LogService;
import de.nebalus.framework.gfw.api.service.logging.Logger;
import de.nebalus.framework.gfw.api.service.module.ModuleService;
import de.nebalus.framework.gfw.modules.dcbot.DCBotModule;

public class MelodyApp {

	private final GFW gfw;

	public MelodyApp(GFW gfw) {
		this.gfw = gfw;
	}

	protected void go() throws Exception {
		// Gets some essential services from the GFW instance
		FileService fileService = gfw.getFileService();
		LogService logService = gfw.getLogService();
		ModuleService moduleService = gfw.getModuleService();

		// Gets the own logger
		Logger appLogger = logService.buildLogger(getClass().getSimpleName(), true);
		
		// Gets the DCBotModule
		DCBotModule dcbot = (DCBotModule) moduleService.getModule(DCBotModule.class);
		
		appLogger.log("Hello World!");
	}

	public GFW getGFW() {
		return gfw;
	}
}

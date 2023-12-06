package de.nebalus.dcbots.melody;

import de.nebalus.framework.gfw.api.GFW;
import de.nebalus.framework.gfw.api.logging.Logger;
import de.nebalus.framework.gfw.api.service.file.FileService;
import de.nebalus.framework.gfw.api.service.module.ModuleService;
import de.nebalus.framework.gfw.modules.dcbot.DCBotModule;

public class MelodyApp {

	private GFW gfw;
	private Logger appLogger;

	public MelodyApp(GFW gfw, Logger appLogger) {
		this.gfw = gfw;
		this.appLogger = appLogger;
	}

	protected void go() throws Exception {
		// Gets some essential services from the GFW instance
		FileService fileService = gfw.getFileService();
		ModuleService moduleService = gfw.getModuleService();

		// Gets the DCBotModule
		DCBotModule dcbot = (DCBotModule) moduleService.getModule(DCBotModule.class);

		// Creates an instance from the MelodyBot
		MelodyBotInstance botInstance = new MelodyBotInstance(this);
		dcbot.loadBotInstance("melody", botInstance);
	}

	public GFW getGFW() {
		return gfw;
	}

	public Logger getLogger() {
		return appLogger;
	}
}

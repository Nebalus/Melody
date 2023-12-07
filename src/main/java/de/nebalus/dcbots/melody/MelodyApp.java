package de.nebalus.dcbots.melody;

import de.nebalus.framework.gfw.api.Application;
import de.nebalus.framework.gfw.api.GFW;
import de.nebalus.framework.gfw.api.file.FileService;
import de.nebalus.framework.gfw.api.logging.Logger;
import de.nebalus.framework.gfw.api.module.ModuleService;
import de.nebalus.framework.gfw.modules.dcbot.DCBotModule;

public class MelodyApp extends Application{

	public MelodyApp(GFW gfw, Logger appLogger) {
		super(gfw, appLogger);
	}

	@Override
	protected void onLoad() throws Exception {
		// Gets some essential services from the GFW instance
		FileService fileService = gfw.getFileService();
		ModuleService moduleService = gfw.getModuleService();

		// Gets the DCBotModule
		DCBotModule dcbot = (DCBotModule) moduleService.getModule(DCBotModule.class);

		// Creates an instance from the MelodyBot
		MelodyBotInstance botInstance = new MelodyBotInstance(this);
		dcbot.loadBotInstance("melody", botInstance);
	}

	@Override
	protected void onUnload() throws Exception {
		// TODO Auto-generated method stub
		
	}
}

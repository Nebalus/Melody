package de.melody.tools.datamanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

import de.melody.tools.ConsoleLogger;
import de.melody.tools.datamanager.files.Config;
import de.melody.tools.datamanager.files.LiteSQL;

public class DataManager {
	
	private final File temp_directory = new File(DataHelper.getCurrentJarPath()+"/temp/");
	private final Config config;
	private final LiteSQL database;
	
	public DataManager() throws Exception{
		//Loads the temp_directory
		if(temp_directory.exists()) {
			FileUtils.cleanDirectory(temp_directory);
		}else {
			temp_directory.mkdir();
		}
		
		for(FileResource resource : FileResource.values()) {
			loadFile(resource);
		}
		
		this.config = new Config(this);
		this.database = new LiteSQL(this);
	}

	private void loadFile(FileResource resource) throws IOException {
		ConsoleLogger.info("FileLoader", "Loading File: " + resource.getInternFilePath());
		if(resource.getFile().exists()) {
			InputStream link = new FileInputStream(resource.getFile());
			File file = new File(getTempDirectory() + resource.parentfile + "OLD_"+resource.getFileName());
			if(!file.exists()) {
				file.mkdirs();
			}	
			Files.copy(link, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		} else {
			File parentfile = new File(DataHelper.getCurrentJarPath() + resource.parentfile);
			if(!parentfile.exists()) {
				parentfile.mkdirs();
			}	
			
			InputStream link = getClass().getResourceAsStream(resource.getInternFilePath());
			Files.copy(link, resource.getFile().getAbsoluteFile().toPath());
		}
	}
	
	public Config getConfig() {
		return config;
	}
	
	public LiteSQL getDatabase() {
		return database;
	}
	
	public File getTempDirectory() {
		return temp_directory;
	}
}

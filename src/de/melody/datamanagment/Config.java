package de.melody.datamanagment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.json.JSONObject;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.tools.Utils;

public class Config {
	@SuppressWarnings("unused")
	private Melody instance;
	private JSONObject jsonobject;
	
	//Values
	public String _bottoken;
	public Boolean _debugmode;
	public String _defaultprefix;
	public Boolean _allowslashcommands;
	public Boolean _autoupdate;
	
	public Config(Melody instance) {
		this.instance = instance;
		File configfile = new File("config.yml");
		try {
			if(configfile.exists()) {
				InputStream link = new FileInputStream(configfile.getAbsoluteFile().getPath());
				File file = new File(Constants.TEMP_DIRECTORY + "/" +"OLD_config.yml");
				Files.copy(link, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
			}else {
				InputStream link = getClass().getResourceAsStream(Constants.CONFIG_URL);
				Files.copy(link, configfile.getAbsoluteFile().toPath());	
			}
			
			this.jsonobject = Utils.getJsonObject(configfile);
			this._bottoken = jsonobject.getString("bottoken");
			this._debugmode = jsonobject.getBoolean("debugmode");
			this._defaultprefix = jsonobject.getString("defaultprefix");
			this._allowslashcommands = jsonobject.getBoolean("allowslashcommands");
			this._autoupdate = jsonobject.getBoolean("autoupdate");
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
}

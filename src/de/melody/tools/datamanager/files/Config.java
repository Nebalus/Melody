package de.melody.tools.datamanager.files;

import java.io.File;
import org.json.JSONObject;

import de.melody.tools.Utils;
import de.melody.tools.datamanager.DataManager;
import de.melody.tools.datamanager.FileResource;

public final class Config {

	// Values
	public String _bottoken = null;
	public Boolean _debugmode = false;
	public String _defaultprefix = "m!";
	public Boolean _allowslashcommands = true;
	public Boolean _autoupdate = true;

	public Config(DataManager manager) throws Exception {
		File configfile = FileResource.CONFIG.getFile();
	
		final JSONObject jsonobject = Utils.getJsonObject(configfile);

		this._bottoken = jsonobject.getString("bottoken");
		this._debugmode = jsonobject.getBoolean("debugmode");
		this._defaultprefix = jsonobject.getString("defaultprefix");
		this._allowslashcommands = jsonobject.getBoolean("allowslashcommands");
		this._autoupdate = jsonobject.getBoolean("autoupdate");

	}
}

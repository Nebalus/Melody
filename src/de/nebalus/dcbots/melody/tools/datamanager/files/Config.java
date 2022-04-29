package de.nebalus.dcbots.melody.tools.datamanager.files;

import java.io.File;
import org.json.JSONObject;

import de.nebalus.dcbots.melody.tools.Utils;
import de.nebalus.dcbots.melody.tools.datamanager.DataManager;
import de.nebalus.dcbots.melody.tools.datamanager.FileResource;

public final class Config {

	// Values
	public String _bottoken = null;
	public Boolean _debugmode = false;
	public Boolean _allowslashcommands = true;
	public Boolean _autoupdate = true;
	public int _ratelimitmaxrequests = 3;
	public Long _ratelimititerationduration = 5000l;

	public Config(DataManager manager) throws Exception {
		File configfile = FileResource.CONFIG.getFile();
	
		final JSONObject jsonobject = Utils.getJsonObject(configfile);

		this._bottoken = jsonobject.getString("bottoken");
		this._debugmode = jsonobject.getBoolean("debugmode");
		this._allowslashcommands = jsonobject.getBoolean("allowslashcommands");
		this._autoupdate = jsonobject.getBoolean("autoupdate");
		this._ratelimitmaxrequests = jsonobject.getInt("ratelimitmaxrequests");
		this._ratelimititerationduration = jsonobject.getLong("ratelimititerationduration");
	}
}

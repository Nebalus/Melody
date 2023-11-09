package old.de.nebalus.dcbots.melody.tools.datamanager.files;

import java.io.File;

import org.json.JSONObject;

import old.de.nebalus.dcbots.melody.tools.Utils;
import old.de.nebalus.dcbots.melody.tools.datamanager.DataManager;
import old.de.nebalus.dcbots.melody.tools.datamanager.FileResource;

public final class Config {

	// Values
	public final String BOTTOKEN;
	public final Boolean DEBUGMODE;
	public final Boolean AUTOUPDATE;
	public final int RATELIMITREQUEST;
	public final Long RATELIMITITERATIONDURATION;
	public final Long MAINDISCORDGUILD;

	public Config(DataManager manager) throws Exception {
		File configfile = FileResource.CONFIG.getFile();

		final JSONObject jsonobject = Utils.getJsonObject(configfile);

		BOTTOKEN = jsonobject.optString("bottoken", null);
		DEBUGMODE = jsonobject.optBoolean("debugmode", false);
		AUTOUPDATE = jsonobject.optBoolean("autoupdate", true);
		RATELIMITREQUEST = jsonobject.optInt("ratelimitmaxrequests", 3);
		RATELIMITITERATIONDURATION = jsonobject.optLong("ratelimititerationduration", 5000l);
		MAINDISCORDGUILD = jsonobject.optLong("maindiscordguild", -1l);
	}
}

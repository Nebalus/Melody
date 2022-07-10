package de.nebalus.dcbots.melody.tools.datamanager.files;

import java.io.File;
import org.json.JSONObject;

import de.nebalus.dcbots.melody.tools.Utils;
import de.nebalus.dcbots.melody.tools.datamanager.DataManager;
import de.nebalus.dcbots.melody.tools.datamanager.FileResource;

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

		this.BOTTOKEN = jsonobject.optString("bottoken", null);
		this.DEBUGMODE = jsonobject.optBoolean("debugmode", false);
		this.AUTOUPDATE = jsonobject.optBoolean("autoupdate", true);
		this.RATELIMITREQUEST = jsonobject.optInt("ratelimitmaxrequests", 3);
		this.RATELIMITITERATIONDURATION = jsonobject.optLong("ratelimititerationduration", 5000l);
		this.MAINDISCORDGUILD = jsonobject.optLong("maindiscordguild", -1l);
	}
}

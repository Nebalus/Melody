package old.de.nebalus.dcbots.melody.tools.datamanager;

import java.io.File;

public enum FileResource {
	CONFIG("/", "config.yml"),
	DATABASE("/", "database.db"),

	IMG_COLORLINE("/img/", "colorline.png"),
	IMG_TRACKINFO("/img/", "trackinfo.png"),
	IMG_DEFAULTAVATAR("/img/", "default-avatar.png"),

	LANG_GERMAN("/lang/", "german.json"),
	LANG_ENGLISH("/lang/", "english.json");

	final String name;
	final String parentfile;

	FileResource(String parentfile, String name) {
		this.name = name;
		this.parentfile = parentfile;
	}

	public String getInternFilePath() {
		return parentfile + name;
	}

	public String getFilePath() {
		return DataHelper.getCurrentJarPath() + parentfile + name;
	}

	public File getFile() {
		return new File(getFilePath());
	}

	public String getFileName() {
		return name;
	}

}

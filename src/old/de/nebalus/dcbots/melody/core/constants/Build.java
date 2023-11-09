package old.de.nebalus.dcbots.melody.core.constants;

public class Build {

	public static String VERSION;
	public static String DATE;
	public static String NAME;
	public static String AUTHOR;

	public Build(String name, String version, String date) {
		VERSION = version;
		DATE = date;
		NAME = name;
		AUTHOR = "Nebalus#1665";
	}

	public Build(String version, String date) {
		NAME = "Melody";
		AUTHOR = "Nebalus#1665";
		VERSION = version;
		DATE = date;
	}

}

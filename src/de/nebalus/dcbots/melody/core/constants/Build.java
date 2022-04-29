package de.nebalus.dcbots.melody.core.constants;

public class Build {

    public static String VERSION;
    public static String DATE;
    public static String NAME;

    public Build(String name, String version, String date) {
        VERSION = version;
        DATE = date;
        NAME = name;
    }

    public Build(String version, String date) {
        NAME = "Melody";
        VERSION = version;
        DATE = date;
    }
}

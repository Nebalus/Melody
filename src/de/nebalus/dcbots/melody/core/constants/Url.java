package de.nebalus.dcbots.melody.core.constants;

import java.net.MalformedURLException;
import java.net.URL;

public class Url {

    public static URL ICON;
    public static URL INVITE;
    public static URL COMMANDS;
    public static URL PLAYLIST;
    public static URL WEBSITE;

    static {
        try {
            ICON = new URL("https://nebalus.github.io/MelodyWeb/assets/images/icon.png");
            INVITE = new URL("https://nebalus.github.io/MelodyWeb/invite");
            COMMANDS = new URL("https://nebalus.github.io/MelodyWeb/commands");
            PLAYLIST = new URL("https://nebalus.github.io/MelodyWeb/playlist?list=");
            WEBSITE = new URL("https://nebalus.github.io/MelodyWeb/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
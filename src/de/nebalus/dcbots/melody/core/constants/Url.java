package de.nebalus.dcbots.melody.core.constants;

import java.net.MalformedURLException;
import java.net.URL;

public class Url 
{

    public static URL ICON;
    public static URL ERROR_ICON;
    public static URL INVITE;
    public static URL COMMANDS;
    public static URL PLAYLIST;
    public static URL WEBSITE;

    static
    {
        try
        {
            ICON = new URL("https://melodybot.ga/assets/images/icon.png");
            ERROR_ICON = new URL("https://melodybot.ga/assets/images/error-icon.png");
            INVITE = new URL("https://melodybot.ga/invite");
            COMMANDS = new URL("https://melodybot.ga/commands");
            PLAYLIST = new URL("https://melodybot.ga/playlist?list=");
            WEBSITE = new URL("https://melodybot.ga/");
        } 
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }
}
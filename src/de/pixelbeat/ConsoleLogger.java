package de.pixelbeat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleLogger {


    final static SimpleDateFormat time = new SimpleDateFormat("<HH:mm:ss> ");
   
    //info
    public static void info(String className, String message) {
        System.out.println(time.format(new Date())+ "[Info] " + className + " : " + message);
    }

    //error
    public static void error(String className, String message) {
        System.out.println(time.format(new Date()) + "[Error] " + className + " : " + message);
    }

    //debug
    public static void debug(String className, String message) {
        System.out.println(time.format(new Date()) + "[Debug] " + className + " : " + message);
    }

    //warning
    public static void warning(String className, String message) {
        System.out.println(time.format(new Date()) + "[Warning] " + className + " : " + message);
    }

}
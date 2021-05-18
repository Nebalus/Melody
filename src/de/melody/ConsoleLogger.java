package de.melody;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleLogger {


    final static SimpleDateFormat time = new SimpleDateFormat("<HH:mm:ss> ");
   
    //info
    public static void info(Object className, Object message) {
        System.out.println(time.format(new Date())+ "[Info] " + className + " : " + message);
    }

    //error
    public static void error(Object className, Object message) {
        System.out.println(time.format(new Date()) + "[Error] " + className + " : " + message);
    }

    //debug
    public static void debug(Object className, Object message) {
        System.out.println(time.format(new Date()) + "[Debug] " + className + " : " + message);
    }

    //warning
    public static void warning(Object className, Object message) {
        System.out.println(time.format(new Date()) + "[Warning] " + className + " : " + message);
    }

}
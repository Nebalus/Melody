package de.melody.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.melody.core.Melody;

public class ConsoleLogger {

    final static SimpleDateFormat time = new SimpleDateFormat("<HH:mm:ss> ");
   
    //info
    public static void info(Object className, Object message) {
        System.out.println(time.format(new Date())+ "[Info] " + className + " : " + message);
    }
    
    public static void info(Object message) {
        System.out.println(time.format(new Date())+ "[Info] : " + message);
    }

    //error
    public static void error(Object className, Object message) {
        System.out.println(time.format(new Date()) + "[Error] " + className + " : " + message);
    }
    
    public static void error( Object message) {
        System.out.println(time.format(new Date()) + "[Error] : " + message);
    }

    //debug
    public static void debug(Object className, Object message) {
    	if(Melody.INSTANCE.config._debugmode) {
    		System.out.println(time.format(new Date()) + "[Debug] " + className + " : " + message);
    	}
    }
    
    public static void debug(Object message) {
    	if(Melody.INSTANCE.config._debugmode) {
    		System.out.println(time.format(new Date()) + "[Debug] : " + message);
    	}
    }

    //warning
    public static void warning(Object className, Object message) {
        System.out.println(time.format(new Date()) + "[Warning] " + className + " : " + message);
    }
    
    public static void warning(Object message) {
        System.out.println(time.format(new Date()) + "[Warning] : " + message);
    }

}
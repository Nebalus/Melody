package de.pixelbeat;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class LiteSQL {
	
	private static Connection conn;
	private static Statement stmt;
	public static void connect() {
		conn = null;
		
			try {
			File file = new File("Datenbank.db");
			if(!file.exists()) {
				file.createNewFile();
			}
			String url = "jdbc:sqlite:"+ file.getPath();
			conn = DriverManager.getConnection(url);
			
			ConsoleLogger.info("SQLDatabase", "Verbindung zur Datenbank hergestellt");
			stmt = conn.createStatement();
			onCreate();
			} catch (SQLException | IOException e1) {
				e1.printStackTrace();
			}
		
	}
	
	
	public static void disconnect() {
		try {
			if(isConnected()) {
				conn.close();
				ConsoleLogger.info("SQLDatabase", "Verbindung zur Datenbank getrennt");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	 public static boolean isConnected() {
	    	return (conn == null ? false : true);
	    }
	 
	 
	 public static Connection getConnection() {
	    	return conn;
	    }
	
	 
	public static void onUpdate(String sql) {
		try {
			stmt.execute(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public static ResultSet onQuery(String sql) {
		try {
			return stmt.executeQuery(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
			return null;
	}
		public static void onCreate() {
			LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTs general(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildid INTEGER, channelid INTEGER, volume INTEGER, pitch INTEGER, speed INTEGER , djrole INTEGER, prefix VARCHAR, ispremium BOOLEAN, voteskip BOOLEAN)");
			LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTs userdata(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, userid INTEGER)");	
		}
}

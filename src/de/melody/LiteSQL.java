package de.melody;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class LiteSQL {
	
	private Connection conn;
	private Statement stmt;
	
	public LiteSQL(){
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
			onUpdate("CREATE TABLE IF NOT EXISTs guilds(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildid INTEGER, channelid INTEGER, volume INTEGER, djrole INTEGER, prefix VARCHAR, voteskip BOOLEAN, staymode BOOLEAN, language VARCHAR, announcesongs BOOLEAN, preventduplicates BOOLEAN, maxusersongs INTEGER, maxqueuelength INTEGER)");
			onUpdate("CREATE TABLE IF NOT EXISTs userdata(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, userid INTEGER, favoritemusic INTEGER, heardtime INTEGER)");	
			/*
			 * token = XXXXXXXXXX / = Example -> dnQW1cgh2s
			 * createdtime is when a user creates a playlist
			 * ownerid is the discord id from the owner who creaded the playlist
			 * 	   tipp: use the id from the userdata category
			 * name | is the name from the playlist
			 * 	   placeholders: 
			 *   	   {username} = 
			 *
			 * privacytype 
			 * 0 = private playlist
			 *     info: only the owner has access to the playlist
			 * 1 = guild playlist 
			 * 	   info: only the users that are in the same guild as the owner has access to the playlist
			 * 2 = public playlist
			 * 	   info: everyone has access to the playlist
			 * 
			 * saved create test esdf 
			 */
			onUpdate("CREATE TABLE IF NOT EXISTs playlist(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, token VARCHAR, createdtime INTEGER, ownerid INTEGER, privacytype INTEGER, name VARCHAR, tracks VARCHAR)");
			onUpdate("CREATE TABLE IF NOT EXISTs track(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, url VARCHAR, name VARCHAR)");
			onUpdate("CREATE TABLE IF NOT EXISTs system(playedmusictime INTEGER)");
		} catch (SQLException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			if(isConnected()) {
				conn.close();
				ConsoleLogger.info("SQLDatabase", "Verbindung zur Datenbank getrennt");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	 public boolean isConnected() {
	    return (conn == null ? false : true);
	 } 
	 
	 public Connection getConnection() {
	    return conn;
	 }
	 
	public void onUpdate(String sql) {
		try {
			stmt.execute(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public ResultSet onQuery(String sql) {
		try {
			return stmt.executeQuery(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
			return null;
	}
}

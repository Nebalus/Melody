package de.melody;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.melody.utils.Utils.ConsoleLogger;

public class LiteSQL {
	
	private Connection conn;
	private Statement stmt;
	
	public LiteSQL(){
		conn = null;
		try {
			File file = new File("datenbank.db");
			if(!file.exists()) {
				file.createNewFile();
			}
			String url = "jdbc:sqlite:"+ file.getPath();
			conn = DriverManager.getConnection(url);
			
			ConsoleLogger.info("SQLDatabase", "Verbindung zur Datenbank hergestellt");
			stmt = conn.createStatement();
			onUpdate("CREATE TABLE IF NOT EXISTs guilds(guildid INTEGER PRIMARY KEY NOT NULL, musicchannelid INTEGER, volume INTEGER, djrole INTEGER, prefix VARCHAR, voteskip BOOLEAN, staymode BOOLEAN, language VARCHAR, announcesongs BOOLEAN, preventduplicates BOOLEAN, maxusersongs INTEGER, maxqueuelength INTEGER, djonly BOOLEAN)");
			onUpdate("CREATE TABLE IF NOT EXISTs userdata(userid INTEGER PRIMARY KEY NOT NULL, favoritemusic INTEGER, heardtime INTEGER, firsttimeheard INTEGER, lasttimeheard INTEGER)");	
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
			onUpdate("CREATE TABLE IF NOT EXISTs playlistinfo(PK_playlistinfo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, token VARCHAR, createdtime INTEGER, ownerid INTEGER, privacytype INTEGER, title VARCHAR)");
			onUpdate("CREATE TABLE IF NOT EXISTs playlistcontent(PK_playlistcontent INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, FK_playlistinfo INTEGER NOT NULL, FK_track INTEGER NOT NULL, position INTEGER)");
			onUpdate("CREATE TABLE IF NOT EXISTs track(PK_track INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, url VARCHAR NOT NULL, title VARCHAR, provider INTEGER NOT NULL DEFAULT 1)");
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
			ConsoleLogger.debug("LITESQL onUpdate", sql);
			stmt.execute(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public ResultSet onQuery(String sql) {
		try {
			ConsoleLogger.debug("LITESQL onQuery", sql);
			return stmt.executeQuery(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
			return null;
	}
}

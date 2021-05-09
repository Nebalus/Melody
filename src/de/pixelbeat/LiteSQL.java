package de.pixelbeat;

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
			onUpdate("CREATE TABLE IF NOT EXISTs general(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildid INTEGER, channelid INTEGER, volume INTEGER, pitch INTEGER, speed INTEGER, djrole INTEGER, prefix VARCHAR, ispremium BOOLEAN, voteskip BOOLEAN, staymode BOOLEAN, language VARCHAR)");
			onUpdate("CREATE TABLE IF NOT EXISTs userdata(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, userid INTEGER)");	
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

package de.melody.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.core.Melody;
import de.melody.datamanagment.LiteSQL;
import de.melody.music.Service;
import de.melody.tools.ConsoleLogger;

public class TrackEntity {
	private LiteSQL database = Melody.INSTANCE._database;
	
	private Service service;
	private String url;
	private String title;
	
	private int trackid;
	public TrackEntity(String title, Service service) {
		this.service = service;
		this.title = title;		
		try {
			PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO tracks(url, title, service) VALUES(?,?,?)");
			ps.setString(1, null);
			ps.setString(2, title);
			ps.setInt(3, getService().getDatabaseID());
			ps.executeUpdate();
			ConsoleLogger.info(ps.getGeneratedKeys().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public TrackEntity(int trackid) {
		try {
			this.trackid = trackid;
			ResultSet rs_track = database.onQuery("SELECT * FROM track WHERE PK_track = "+trackid);	
			if(rs_track.next()) {
				this.service = Service.valueOfID(rs_track.getInt("service"));
				this.title = rs_track.getString("title");
				if(getService().equals(Service.YOUTUBE)) {
					this.url = getService().getShortURL() + rs_track.getString("url");
				}
			}	
		}catch(SQLException e) {
			e.printStackTrace();			
		}
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public Service getService() {
		return this.service;
	}

}

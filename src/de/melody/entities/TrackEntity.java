package de.melody.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.core.Melody;
import de.melody.datamanager.LiteSQL;
import de.melody.music.Service;

public class TrackEntity {
	private LiteSQL database = Melody.INSTANCE.getDatabase();
	
	private Service service;
	private String url;
	private String title;
	
	public TrackEntity(int trackid) {
		if(database.isConnected()) {
			try {
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

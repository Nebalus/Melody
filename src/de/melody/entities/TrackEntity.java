package de.melody.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.LiteSQL;
import de.melody.core.Melody;

public class TrackEntity {
	private LiteSQL database = Melody.INSTANCE.getDatabase();
	
	private Provider provider;
	private String url;
	private String title;
	
	
	public TrackEntity(int trackid) {
		if(database.isConnected()) {
			try {
				ResultSet rs_track = database.onQuery("SELECT * FROM track WHERE PK_track = "+trackid);	
				if(rs_track.next()) {
					this.provider = Provider.valueOfID(rs_track.getInt("provider"));
					this.title = rs_track.getString("title");
					if(getProvider().equals(Provider.YOUTUBE)) {
						this.url = getProvider().shorturl + rs_track.getString("url");
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
	
	public Provider getProvider() {
		return this.provider;
	}
	
	public enum Provider{
		YOUTUBE(1,"https://www.youtube.com/watch?v="),
		SOUNDCLOUD(2,null);
		
		int providerid;
		String shorturl;
		
		Provider(int providerid, String shorturl) {
			this.providerid = providerid;
			this.shorturl = shorturl;
		}
		
		public static Provider valueOfID(int id) {
			for(Provider prov : Provider.values()) {
				if(prov.providerid == id) {
					return prov;
				}
			}
			return YOUTUBE;
		}
	}
}

package de.melody.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.LiteSQL;
import de.melody.Melody;
import de.melody.utils.ID_Manager;

public class PlaylistEntity {

	private String token = ID_Manager.generateID();
	private Long createdtime = System.currentTimeMillis();
	private Long ownerid = 0l;
	private String name = "Unknown";
	private String urls = "{}";
	
	private Long expiretime;
	
	private Melody pixelbeat = Melody.INSTANCE;
	private LiteSQL database = pixelbeat.getDatabase();
	
	public PlaylistEntity(int playlistlistid) {
		this.expiretime = System.currentTimeMillis() + Melody.expiretime;
		if(database.isConnected()) {
			try {
				ResultSet rs = database.onQuery("SELECT * FROM playlist WHERE id = " + playlistlistid);	
				if(rs.next()) {
					token = rs.getString("token");	
					createdtime = rs.getLong("createdtime");
					ownerid = rs.getLong("createdtime");
					name = rs.getString("name");
					urls = rs.getString("urls");
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Long getExpireTime() {
		return this.expiretime;
	}
}

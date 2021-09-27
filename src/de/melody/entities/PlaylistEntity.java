package de.melody.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.LiteSQL;
import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.utils.Utils.IDGenerator;

public class PlaylistEntity {

	private String token;
	private Long createdtime;
	private Long ownerid = 0l;
	private String name;
	
	private Long expiretime;
	
	private Melody melody = Melody.INSTANCE;
	private LiteSQL database = melody.getDatabase();
	
	public PlaylistEntity(int playlistlistid) {
		this.expiretime = System.currentTimeMillis() + Constants.ENTITYEXPIRETIME;
		if(database.isConnected()) {
			try {
				ResultSet rs_playlistinfo = database.onQuery("SELECT * FROM playlistinfo WHERE id = " + playlistlistid);	
				if(rs_playlistinfo.next()) {
					this.token = rs_playlistinfo.getString("token");	
					this.createdtime = rs_playlistinfo.getLong("createdtime");
					this.ownerid = rs_playlistinfo.getLong("ownerid");
					this.name = rs_playlistinfo.getString("name");
					ResultSet rs_playlistcontent = database.onQuery("SELECT * FROM playlistcontent WHERE id = " + playlistlistid);
					while(rs_playlistcontent.next()) {
						
					}
				}else {
					this.token = IDGenerator.generateID();
					this.createdtime = System.currentTimeMillis();
					this.name = "Unknown - "+this.token;
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public PlaylistEntity() {
		this.expiretime = System.currentTimeMillis() + Constants.ENTITYEXPIRETIME;
		this.token = IDGenerator.generateID();
		this.createdtime = System.currentTimeMillis();
		this.name = "Unknown - "+this.token;
		
	}
	
	public Long getExpireTime() {
		return this.expiretime;
	}
}

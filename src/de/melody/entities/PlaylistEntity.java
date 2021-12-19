package de.melody.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.datamanager.LiteSQL;
import de.melody.utils.Utils.IDGenerator;

public class PlaylistEntity {

	private String token;
	private Long createdtime;
	private Long ownerid = 0l;
	private String title;
	private HashMap<Integer, TrackEntity> content;
	private Long expiretime;
	
	private Melody melody = Melody.INSTANCE;
	private LiteSQL database = melody.getDatabase();
	
	public PlaylistEntity(int playlistlistid) {
		this.expiretime = System.currentTimeMillis() + Constants.ENTITYEXPIRETIME;
		if(database.isConnected()) {
			try {
				ResultSet rs_playlistinfo = database.onQuery("SELECT * FROM playlistinfo, playlistcontent WHERE PK_playlistinfo = " + playlistlistid);	
				if(rs_playlistinfo.next()) {
					this.token = rs_playlistinfo.getString("token");	
					this.createdtime = rs_playlistinfo.getLong("createdtime");
					this.ownerid = rs_playlistinfo.getLong("ownerid");
					this.title = rs_playlistinfo.getString("title");
					ResultSet rs_playlistcontent = database.onQuery("SELECT * FROM playlistcontent WHERE FK_playlistinfo = " + playlistlistid);
					while(rs_playlistcontent.next()) {
						content.put(rs_playlistcontent.getInt("position"),new TrackEntity(rs_playlistcontent.getInt("FK_track")));
					}
				}else {
					this.token = IDGenerator.generateID();
					this.createdtime = System.currentTimeMillis();
					this.title = "Unknown - "+this.token;
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public HashMap<Integer, TrackEntity> getContent(){
		return content;
	}
	
	public Long getOwnerID() {
		return this.ownerid;
	}
	
	public Long getCreatedTime() {
		return this.createdtime;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public PlaylistEntity() {
		this.expiretime = System.currentTimeMillis() + Constants.ENTITYEXPIRETIME;
		this.token = IDGenerator.generateID();
		this.createdtime = System.currentTimeMillis();
		this.title = "Unknown - "+this.token;
	}
	
	public Long getExpireTime() {
		return this.expiretime;
	}
}

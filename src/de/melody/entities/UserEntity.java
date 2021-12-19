package de.melody.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.datamanager.LiteSQL;
import de.melody.utils.Utils.ConsoleLogger;
import net.dv8tion.jda.api.entities.User;

public class UserEntity {
	
	private int favoriteplaylistid = 0;
	private Long userid;
	private Long heardtime = 0l;
	private Long firsttimeheard = 0l;
	private Long lasttimeheard = 0l;	
	
	private Long expiretime = System.currentTimeMillis() + Constants.ENTITYEXPIRETIME;
	private Boolean needtoexport = false;
	
	private Melody melody = Melody.INSTANCE;
	private LiteSQL database = melody.getDatabase();
	
	public UserEntity(User user) {
		this.userid = user.getIdLong();
		this.lasttimeheard = System.currentTimeMillis();
		this.firsttimeheard = System.currentTimeMillis();
		if(database.isConnected()) {
			try {
				ResultSet rs = database.onQuery("SELECT * FROM userdata WHERE PK_userid = " + userid);	
				if(rs.next()) {
					favoriteplaylistid = rs.getInt("favoriteplaylist");
					heardtime = rs.getLong("heardtime");
					firsttimeheard = rs.getLong("firsttimeheard");
					lasttimeheard = rs.getLong("lasttimeheard");
				}else {
					PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO userdata(PK_userid,firsttimeheard) VALUES(?,?)");
					ps.setLong(1, userid);
					ps.setLong(2, firsttimeheard);
					ps.executeUpdate();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Long getHeardTime() {
		renewExpireTime();
		return this.heardtime;
	}
	
	public void setHeardTime(Long newheardtime) {
		heardtime = newheardtime;
		lasttimeheard = System.currentTimeMillis();
		update();
	}
	
	public Long getUserId() {
		renewExpireTime();
		return this.userid;
	}
	
	public int getFavoritePlaylistId() {
		renewExpireTime();
		return this.favoriteplaylistid;
	}
	
	public void setFavoritePlaylistId(int newplaylistid) {
		favoriteplaylistid = newplaylistid;
		update();
	}
	
	private void update() {
		needtoexport = true;
		renewExpireTime();
	}
	
	public Long getExpireTime() {
		return this.expiretime;
	}
	
	public Boolean getNeedToExport() {
		return this.needtoexport;
	}
	
	private void renewExpireTime() {
		this.expiretime = System.currentTimeMillis() + Constants.ENTITYEXPIRETIME;
	}
	
	public boolean export() {
		if(database.isConnected()) {
			if(needtoexport) {
				try {
					PreparedStatement ps = database.getConnection().prepareStatement("UPDATE userdata SET "
							+ "favoriteplaylist = ?,"
							+ "heardtime = ?,"
							+ "lasttimeheard = ? WHERE PK_userid = ?");
					ps.setInt(1, favoriteplaylistid);
					ps.setLong(2, heardtime);
					ps.setLong(3, lasttimeheard);
					ps.setLong(4, userid);
					ps.executeUpdate();
					ConsoleLogger.debug("export user", userid);
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				needtoexport = false;
				return true;
			}
			return true;
		}
		return false;
	}
}

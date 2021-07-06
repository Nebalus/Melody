package de.melody.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.ConsoleLogger;
import de.melody.LiteSQL;
import de.melody.Melody;
import net.dv8tion.jda.api.entities.User;

public class UserEntity {
	
	private int favoritemusicid = 0;
	private Long userid;
	private Long heardtime = 0l;
	
	private Long expiretime = System.currentTimeMillis() + Melody.expiretime;
	private Boolean needtoexport = false;
	
	private Melody pixelbeat = Melody.INSTANCE;
	private LiteSQL database = pixelbeat.getDatabase();
	
	public UserEntity(User user) {
		this.userid = user.getIdLong();
		if(database.isConnected()) {
			try {
				ResultSet rs = database.onQuery("SELECT * FROM userdata WHERE userid = " + userid);	
				if(rs.next()) {
					favoritemusicid = rs.getInt("favoritemusic");
					heardtime = rs.getLong("heardtime");
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
		update();
	}
	
	public Long getUserId() {
		renewExpireTime();
		return this.userid;
	}
	
	public int getFavoriteMusicId() {
		renewExpireTime();
		return this.favoritemusicid;
	}
	public void setFavoriteMusicId(int newplaylistid) {
		favoritemusicid = newplaylistid;
		update();
	}
	
	private void update() {
		needtoexport = true;
		this.expiretime = System.currentTimeMillis() + Melody.expiretime;
	}
	
	public Long getExpireTime() {
		return this.expiretime;
	}
	
	public Boolean getNeedToExport() {
		return this.needtoexport;
	}
	
	private void renewExpireTime() {
		this.expiretime = System.currentTimeMillis() + Melody.expiretime;
	}
	
	public boolean exportData() {
		if(database.isConnected()) {
			if(needtoexport) {
				try {
					PreparedStatement ps = database.getConnection().prepareStatement("UPDATE userdata SET "
							+ "favoritemusic = ?, heardtime = ? WHERE userid = ?");
					ps.setInt(1, favoritemusicid);
					ps.setLong(2, heardtime);
					ps.setLong(3, userid);
					ps.executeUpdate();
					ConsoleLogger.info("export user", userid);
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

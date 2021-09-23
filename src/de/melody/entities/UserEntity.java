package de.melody.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.LiteSQL;
import de.melody.core.Config;
import de.melody.core.Melody;
import de.nebalus.botbuilder.console.ConsoleLogger;
import net.dv8tion.jda.api.entities.User;

public class UserEntity {
	
	private int favoritemusicid = 0;
	private Long userid;
	private Long heardtime = 0l;
	private Long firsttimeheard = 0l;
	private Long lasttimeheard = 0l;	
	
	private Long expiretime = System.currentTimeMillis() + Config.ENTITYEXPIRETIME;
	private Boolean needtoexport = false;
	
	private Melody melody = Melody.INSTANCE;
	private LiteSQL database = melody.getDatabase();
	
	public UserEntity(User user) {
		this.userid = user.getIdLong();
		this.lasttimeheard = System.currentTimeMillis();
		this.firsttimeheard = System.currentTimeMillis();
		if(database.isConnected()) {
			try {
				ResultSet rs = database.onQuery("SELECT * FROM userdata WHERE userid = " + userid);	
				if(rs.next()) {
					favoritemusicid = rs.getInt("favoritemusic");
					heardtime = rs.getLong("heardtime");
					firsttimeheard = rs.getLong("firsttimeheard");
					lasttimeheard = rs.getLong("lasttimeheard");
				}else {
					PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO userdata(userid,firsttimeheard) VALUES(?,?)");
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
		renewExpireTime();
	}
	
	public Long getExpireTime() {
		return this.expiretime;
	}
	
	public Boolean getNeedToExport() {
		return this.needtoexport;
	}
	
	private void renewExpireTime() {
		this.expiretime = System.currentTimeMillis() + Config.ENTITYEXPIRETIME;
	}
	
	public boolean export() {
		if(database.isConnected()) {
			if(needtoexport) {
				try {
					PreparedStatement ps = database.getConnection().prepareStatement("UPDATE userdata SET "
							+ "favoritemusic = ?,"
							+ "heardtime = ?,"
							+ "lasttimeheard = ? WHERE userid = ?");
					ps.setInt(1, favoritemusicid);
					ps.setLong(2, heardtime);
					ps.setLong(3, lasttimeheard);
					ps.setLong(4, userid);
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

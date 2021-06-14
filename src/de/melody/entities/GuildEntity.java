package de.melody.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.ConsoleLogger;
import de.melody.LiteSQL;
import de.melody.Melody;
import de.melody.speechpackets.Languages;

public class GuildEntity {
	
	private Long guildid;
	private Long channelid;
	private int volume = 50;
	private double pitch = 1.0;
	private double speed = 1.0;
	private Long djroleid;
	private boolean voteskip = false;
	private boolean staymode = false;
	private Languages language = Languages.ENGLISH;
	
	private Long expiretime = System.currentTimeMillis() + Melody.expiretime;
	private Boolean needtoexport = false;
	
	private Melody pixelbeat = Melody.INSTANCE;
	private LiteSQL database = pixelbeat.getDatabase();
	
	public GuildEntity(Long guildid) {
		this.guildid = guildid;
		if(database.isConnected()) {
			try {
				ResultSet rs = database.onQuery("SELECT * FROM general WHERE guildid = " + guildid);	
				if(rs.next()) {
					channelid = rs.getLong("channelid");	
					if(rs.getInt("volume") > 0) {
						volume = rs.getInt("volume");
					}
					pitch = rs.getInt("pitch");
					if(rs.getDouble("speed") > 0) {
						speed = rs.getDouble("speed");
					}
					djroleid = rs.getLong("djrole");
					voteskip = rs.getBoolean("voteskip");
					staymode = rs.getBoolean("staymode");
					if(rs.getString("language") != null) {
						language = Languages.getLanguage(rs.getString("language"));
					}
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Long getGuildId() {
		renewExpireTime();
		return this.guildid;
	}
	
	public Long getChannelId() {
		renewExpireTime();
		return this.channelid;
	}
	public void setChannelId(Long newchannelid) {
		this.channelid = newchannelid;
		update();
	}
	
	public int getVolume() {
		renewExpireTime();
		return this.volume;
	}
	public void setVolume(int newvolume) {
		this.volume = newvolume;
		update();
	}
	
	public double getPitch() {
		renewExpireTime();
		return this.pitch;
	}
	
	public double getSpeed() {
		renewExpireTime();
		return this.speed;
	}
	public void setSpeed(Double newspeed) {
		this.speed = newspeed;
		update();
	}
	
	public Long getDjRoleId() {
		renewExpireTime();
		return this.djroleid;
	}
	
	public Boolean isVoteSkip() {
		renewExpireTime();
		return this.voteskip;
	}
	
	public Boolean is24_7() {
		renewExpireTime();
		return this.staymode;
	}
	public void set24_7(Boolean new24_7) {
		this.staymode = new24_7;
		update();
	}
	
	public Languages getLanguage() {
		renewExpireTime();
		return this.language;
	}
	
	public Long getExpireTime() {
		return this.expiretime;
	}
	
	public Boolean getNeedToExport() {
		return this.needtoexport;
	}
	
	private void update() {
		this.needtoexport = true;
		renewExpireTime();
	}
	
	private void renewExpireTime() {
		this.expiretime = System.currentTimeMillis() + Melody.expiretime;
	}
	
	public boolean exportData() {
		if(database.isConnected()) {
			if(needtoexport) {
				try {
					PreparedStatement ps = database.getConnection().prepareStatement("UPDATE general SET "
							+ "channelid = ?,"
							+ "volume = ?,"
							+ "pitch = ?,"
							+ "speed = ?,"
							+ "djrole = ?,"
							+ "voteskip = ?,"
							+ "staymode = ?,"
							+ "language = ? WHERE guildid = ?");
					ps.setLong(1, channelid);
					ps.setInt(2, volume);
					ps.setDouble(3, pitch);
					ps.setDouble(4, speed);
					ps.setLong(5, djroleid);
					ps.setBoolean(6, voteskip);
					ps.setBoolean(7, staymode);
					ps.setString(8, language.getCode());
					ps.setLong(9, guildid);
					ps.executeUpdate();
					ConsoleLogger.info("export guild", guildid);
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

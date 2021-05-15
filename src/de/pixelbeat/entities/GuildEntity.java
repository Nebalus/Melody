package de.pixelbeat.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.pixelbeat.ConsoleLogger;
import de.pixelbeat.LiteSQL;
import de.pixelbeat.PixelBeat;
import de.pixelbeat.speechpackets.Languages;

public class GuildEntity {
	
	private Long guildid;
	private Long channelid;
	private int volume = 50;
	private double pitch = 1.0;
	private double speed = 1.0;
	private Long djroleid;
	private String prefix = "pb!";
	private boolean ispremium = false;
	private boolean voteskip = false;
	private boolean staymode = false;
	private Languages language = Languages.ENGLISH;
	
	private Long expiretime = System.currentTimeMillis() + PixelBeat.expiretime;
	private Boolean needtoexport = false;
	
	private PixelBeat pixelbeat = PixelBeat.INSTANCE;
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
					if(rs.getString("prefix") != null) {
						prefix = rs.getString("prefix");
					}
					ispremium = rs.getBoolean("ispremium");
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
		return this.guildid;
	}
	
	public Long getChannelId() {
		return this.channelid;
	}
	public void setChannelId(Long newchannelid) {
		this.channelid = newchannelid;
		update();
	}
	
	public int getVolume() {
		return this.volume;
	}
	public void setVolume(int newvolume) {
		this.volume = newvolume;
		update();
	}
	
	public double getPitch() {
		return this.pitch;
	}
	
	public double getSpeed() {
		return this.speed;
	}
	public void setSpeed(Double newspeed) {
		this.speed = newspeed;
		update();
	}
	
	public Long getDjRoleId() {
		return this.djroleid;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	public void setPrefix(String newprefix) {
		this.prefix = newprefix;
		update();
	}
	
	public Boolean isPremium() {
		return this.ispremium;
	}
	
	public Boolean isVoteSkip() {
		return this.voteskip;
	}
	
	public Boolean is24_7() {
		return this.staymode;
	}
	
	public Languages getLanguage() {
		return language;
	}
	
	public Long getExpireTime() {
		return this.expiretime;
	}
	
	public Boolean getNeedToExport() {
		return this.needtoexport;
	}
	
	private void update() {
		needtoexport = true;
		this.expiretime = System.currentTimeMillis() + PixelBeat.expiretime;
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
							+ "prefix = ?,"
							+ "ispremium = ?,"
							+ "voteskip = ?,"
							+ "staymode = ?,"
							+ "language = ? WHERE guildid = ?");
					ps.setLong(1, channelid);
					ps.setInt(2, volume);
					ps.setDouble(3, pitch);
					ps.setDouble(4, speed);
					ps.setLong(5, djroleid);
					ps.setString(6, prefix);
					ps.setBoolean(7, ispremium);
					ps.setBoolean(8, voteskip);
					ps.setBoolean(9, staymode);
					ps.setString(10, language.getCode());
					ps.setLong(11, guildid);
					ps.executeUpdate();
					ConsoleLogger.info("export", guildid);
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
			return true;
		}
		return false;
	}
}

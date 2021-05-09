package de.pixelbeat.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.pixelbeat.ConsoleLogger;
import de.pixelbeat.PixelBeat;
import de.pixelbeat.speechpackets.Languages;

public class GuildEntity {
	
	private Long guildid;
	private Long channelid;
	private int volume = 50;
	private int pitch;
	private int speed;
	private Long djroleid;
	private String prefix = "pb!";
	private boolean ispremium;
	private boolean voteskip;
	private boolean staymode;
	private Languages language = Languages.ENGLISH;
	private Long expiretime;
	
	private PixelBeat pixelbeat = PixelBeat.INSTANCE;
	
	public GuildEntity(Long guildid) {
		this.guildid = guildid;
		this.expiretime = System.currentTimeMillis() + PixelBeat.expiretime;
		if(pixelbeat.getDatabase().isConnected()) {
			try {
				ResultSet rs = pixelbeat.getDatabase().onQuery("SELECT * FROM general WHERE guildid = " + guildid);	
				if(rs.next()) {
					channelid = rs.getLong("channelid");	
					if(rs.getInt("volume") >= 0) {
						volume = rs.getInt("volume");
					}
					pitch = rs.getInt("pitch");
					speed = rs.getInt("speed");
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
	}
	
	public int getVolume() {
		return this.volume;
	}
	public void setVolume(int newvolume) {
		this.volume = newvolume;
	}
	
	public int getPitch() {
		return this.pitch;
	}
	
	public int getSpeed() {
		return this.speed;
	}
	
	public Long getDjRoleId() {
		return this.djroleid;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	public void setPrefix(String newprefix) {
		this.prefix = newprefix;
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
	
	public boolean exportData() {
		if(pixelbeat.getDatabase().isConnected()) {
			ConsoleLogger.info("export", guildid);
			try {
				PreparedStatement ps = pixelbeat.getDatabase().getConnection().prepareStatement("UPDATE general SET "
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
				ps.setInt(3, pitch);
				ps.setInt(4, speed);
				ps.setLong(5, djroleid);
				ps.setString(6, prefix);
				ps.setBoolean(7, ispremium);
				ps.setBoolean(8, voteskip);
				ps.setBoolean(9, staymode);
				ps.setString(10, language.getCode());
				ps.setLong(11, guildid);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			expiretime = System.currentTimeMillis() + PixelBeat.expiretime;
			return true;
		}
		return false;
	}
}

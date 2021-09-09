package de.melody.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.melody.LiteSQL;
import de.melody.core.Config;
import de.melody.core.Melody;
import de.melody.speechpackets.Languages;
import de.nebalus.botbuilder.console.ConsoleLogger;
import net.dv8tion.jda.api.entities.Guild;

public class GuildEntity {
	
	private Guild guild;
	private Long channelid;
	private int volume = 50;
	private Long djroleid = 0l;
	private String prefix = "m!";
	private boolean voteskip = false;
	private boolean staymode = false;
	private boolean announcesongs = true;
	private boolean preventduplicates = false;
	private Languages language = Languages.ENGLISH;
	
	private Long expiretime = System.currentTimeMillis() + Config.ENTITYEXPIRETIME;
	private Boolean needtoexport = false;
	
	private Melody melody = Melody.INSTANCE;
	private LiteSQL database = melody.getDatabase();
	
	public GuildEntity(Guild guild) {
		this.guild = guild;
		if(database.isConnected()) {
			try {
				ResultSet rs = database.onQuery("SELECT * FROM guilds WHERE guildid = " + getGuildId());	
				if(rs.next()) {
					channelid = rs.getLong("channelid");	
					if(rs.getInt("volume") > 0) {
						volume = rs.getInt("volume");
					}
					djroleid = rs.getLong("djrole");
					if(rs.getString("prefix") != null) {
						prefix = rs.getString("prefix");
					}
					if(rs.getString("voteskip") != null) {
						voteskip = rs.getBoolean("voteskip");	
					}
					if(rs.getString("staymode") != null) {
						staymode = rs.getBoolean("staymode");	
					}
					if(rs.getString("announcesongs") != null) {
						announcesongs = rs.getBoolean("announcesongs");	
					}
					if(rs.getString("preventduplicates") != null) {
						preventduplicates = rs.getBoolean("preventduplicates");	
					}
					if(rs.getString("language") != null) {
						language = Languages.getLanguage(rs.getString("language"));
					}
				}else {
					PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO guilds(guildid) VALUES(?)");
					ps.setLong(1, getGuildId());
					ps.executeUpdate();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Long getGuildId() {
		renewExpireTime();
		return this.guild.getIdLong();
	}
	
	public Guild getGuild() {
		renewExpireTime();
		return this.guild;
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
	public Long getDjRoleId() {
		renewExpireTime();
		return this.djroleid;
	}
	
	public String getPrefix() {
		renewExpireTime();
		return this.prefix;
	}
	public void setPrefix(String newprefix) {
		this.prefix = newprefix;
		update();
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
	
	public void setLanguage(Languages newlanguage) {
		this.language = newlanguage;
		update();
	}
	
	public Languages getLanguage() {
		renewExpireTime();
		return this.language;
	}
	
	public Boolean canAnnounceSongs() {
		renewExpireTime();
		return this.announcesongs;
	}
	
	public void setAnnounceSongs(Boolean newannouncesongs) {
		this.announcesongs = newannouncesongs;
		update();
	}
	
	public Boolean isPreventDuplicates() {
		renewExpireTime();
		return this.preventduplicates;
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
		this.expiretime = System.currentTimeMillis() + Config.ENTITYEXPIRETIME;
	}
	
	public boolean exportData() {
		if(database.isConnected()) {
			if(needtoexport) {
				try {
					PreparedStatement ps = database.getConnection().prepareStatement("UPDATE guilds SET "
							+ "channelid = ?,"
							+ "volume = ?,"
							+ "djrole = ?,"
							+ "prefix = ?,"
							+ "voteskip = ?,"
							+ "staymode = ?,"
							+ "language = ?,"
							+ "announcesongs = ?,"
							+ "preventduplicates = ? WHERE guildid = ?");
					ps.setLong(1, channelid);
					ps.setInt(2, volume);
					ps.setLong(3, djroleid);
					ps.setString(4, prefix);
					ps.setBoolean(5, voteskip);
					ps.setBoolean(6, staymode);
					ps.setString(7, language.getCode());
					ps.setBoolean(8, announcesongs);
					ps.setBoolean(9, preventduplicates);
					ps.setLong(10, getGuildId());
					ps.executeUpdate();
					ConsoleLogger.info("export guild", getGuildId());
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

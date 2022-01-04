package de.melody.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.datamanagment.LiteSQL;
import de.melody.entities.reacts.ReactionManager;
import de.melody.speechpackets.Languages;
import de.melody.tools.ConsoleLogger;
import de.melody.tools.DataType;
import de.melody.tools.Utils.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class GuildEntity{
	
	private Guild guild;
	
	//Options
	private int volume = 50;
	private String prefix = Melody.INSTANCE._config._defaultprefix;
	private int playtime = 0;
	private Long lastaudiochannelid = 0l;
	private boolean djonly = false;
	private boolean voteskip = false;
	private boolean staymode = false;
	private boolean announcesongs = true;
	private boolean preventduplicates = false;
	private Languages language = Languages.ENGLISH;
	
	private Long expiretime = System.currentTimeMillis() + Constants.ENTITYEXPIRETIME;
	private Boolean needtoexport = false;
	
	private ReactionManager reactionmanager;
	private Melody melody = Melody.INSTANCE;
	private LiteSQL database = melody._database;
	
	public GuildEntity(Guild guild) {
		this.guild = guild;
		this.reactionmanager = new ReactionManager();
		if(database.isConnected()) {
			try {
				ResultSet rsguild = database.onQuery("SELECT * FROM guilds WHERE PK_guildid = " + getGuildId());	
				if(rsguild.next()) {
					if(rsguild.getString("prefix") != null) {
						prefix = rsguild.getString("prefix");
					}
					if(rsguild.getString("language") != null) {
						language = Languages.getLanguage(rsguild.getInt("language"));
					}
					if(rsguild.getInt("playtime") > 0) {
						playtime = rsguild.getInt("playtime");
					}
					if(rsguild.getLong("lastaudiochannel") > 0) {
						lastaudiochannelid = rsguild.getLong("lastaudiochannel");
					}
				}else {
					boolean mentioned = false;
					for(TextChannel tc : guild.getTextChannels()) {
						if(!mentioned) {
							try {
								tc.sendMessage("Hello everybody, i'm "+guild.getJDA().getSelfUser().getAsMention()+" "+guild.getJDA().getEmoteById(Emoji.HEY_GUYS).getAsMention()+"\n"
										+ " \n"
										+ " `-` My prefix on "+guild.getName()+" is `"+getPrefix()+"`\n"
										+ " `-` If you do not understand how I work then you can see all my commands by typing `"+getPrefix()+"help`\n"
										+ " `-` When you dont like something in my config then you can easyly change it by typing `"+getPrefix()+"config help`\n"
										+ " \n"
										+ "**Otherwise have fun listening to the music from my service** "+ Emoji.MUSIC_NOTE+" \n"
										+ "PS: Thanks a lot for your support, that you added me to your discord server! "+Emoji.SPARKLING_HEART).queue();
								mentioned = true;
							}catch(InsufficientPermissionException e) {}
						}
					}
					//loads the guild in the database
					PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO guilds(PK_guildid,firsttimeloaded) VALUES(?,?)");
					ps.setLong(1, getGuildId());
					ps.setLong(2, System.currentTimeMillis());
					ps.executeUpdate();
					update();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
			try {
				ResultSet rsoptions = database.onQuery("SELECT * FROM guildoptions WHERE PK_guildid = " + getGuildId());	
				while(rsoptions.next()) {
					if(rsoptions.getString("content") != null) {
						switch(GuildOptions.getFromDatabaseID(rsoptions.getInt("typeid"))) {
							case ANNOUNCESONGS:
								announcesongs = rsoptions.getBoolean("content");
								break;
							case VOLUME:
								volume = rsoptions.getInt("content");
								break;
							case PREVENTDUPLICATES:
								preventduplicates = rsoptions.getBoolean("content");
								break;
							case STAYMODE:
								staymode = rsoptions.getBoolean("content");
								break;
							case DJONLY:
								djonly = rsoptions.getBoolean("content");
								break;
							case VOTESKIP:
								voteskip = rsoptions.getBoolean("content");
							default:
								break;		
						}
					}
				}
			}catch(SQLException e) {	
				e.printStackTrace();
			}
		}
	}
	
	public ReactionManager getReactionManager() {
		return reactionmanager;
	}
	
	public Long getGuildId() {
		renewExpireTime();
		return this.guild.getIdLong();
	}
	
	public Guild getGuild() {
		renewExpireTime();
		return this.guild;
	}
	
	public int getPlayTime() {
		renewExpireTime();
		return this.playtime;
	}
	
	public void setPlayTime(int newplaytime) {
		playtime = newplaytime;
		update();
	}
	
	public Long getLastAudioChannelId() {
		renewExpireTime();
		return this.lastaudiochannelid;
	}
	
	public VoiceChannel getLastAudioChannel() {
		renewExpireTime();
		VoiceChannel channel;
		if((channel = guild.getVoiceChannelById(this.lastaudiochannelid)) != null) {
			return channel;
		}
		return null;
	}
	
	public void setLastAudioChannelId(Long lastaudiochannelid) {
		this.lastaudiochannelid = lastaudiochannelid;
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
	
	public Boolean isDjOnly() {
		renewExpireTime();
		return this.djonly;
	}
	
	public Boolean isMemberDJ(Member member) {
		List<Role> roles = member.getRoles();
		if(isDjOnly()) {
			for(Role role : roles) {
				/*if(role.getIdLong() == djroleid && member.hasPermission(Permission.MANAGE_SERVER)) {
					return true;
				}*/
			}	
			return false;
		}else {
			return true;
		}
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
		this.expiretime = System.currentTimeMillis() + Constants.ENTITYEXPIRETIME;
	}
	
	public boolean export() {
		if(database.isConnected()) {
			if(needtoexport) {
				try {
					PreparedStatement ps = database.getConnection().prepareStatement("UPDATE guilds SET "
							+ "volume = ?,"
							+ "prefix = ?,"
							+ "voteskip = ?,"
							+ "staymode = ?,"
							+ "language = ?,"
							+ "announcesongs = ?,"
							+ "preventduplicates = ?,"
							+ "playtime = ?,"
							+ "lastaudiochannel = ?,"
							+ "djonly = ? WHERE PK_guildid = ?");
					ps.setInt(1, volume);
					ps.setString(2, prefix);			
					ps.setBoolean(3, voteskip);
					ps.setBoolean(4, staymode);
					ps.setInt(5, language.getDatabaseID());
					ps.setBoolean(6, announcesongs);
					ps.setBoolean(7, preventduplicates);
					ps.setInt(8, playtime);
					ps.setLong(9, lastaudiochannelid);
					ps.setBoolean(10, djonly);
					ps.setLong(11, getGuildId());
					ps.executeUpdate();
					ConsoleLogger.debug("export guild", getGuildId());
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				needtoexport = false;
			}
			return true;
		}
		return false;
	}
	
	pu
	
	private enum GuildOptions {

		DJROLE(1, DataType.LONG),
		PREVENTDUPLICATES(2, DataType.BOOLEAN),
		ANNOUNCESONGS(3, DataType.BOOLEAN),
		VOTESKIP(4, DataType.BOOLEAN),
		VOLUME(5, DataType.INT),
		STAYMODE(6, DataType.BOOLEAN),
		DJONLY(7, DataType.BOOLEAN),
		LANGUAGE(8, DataType.INT),
		PREFIX(9, DataType.VARCHAR),
		PLAYTIME(10, DataType.INT),
		LASTAUDIOCHANNELID(11, DataType.LONG);
		
		final int databaseid;
		final DataType datatype;
		
		GuildOptions(int databaseid, DataType datatype){
			this.databaseid = databaseid; 
			this.datatype = datatype;
		}
		
		public DataType getDataType() {
			return datatype;
		}
		
		public int getDatabaseID() {
			return databaseid;
		}
		
		public static GuildOptions getFromDatabaseID(int id) {
			for(GuildOptions options : GuildOptions.values()) {
				if(options.getDatabaseID() == id) {
					return options;
				}
			}
			return null;
		}
	}
}

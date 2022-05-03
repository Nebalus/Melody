package de.melody.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.datamanagment.LiteSQL;
import de.melody.entities.reacts.ReactionManager;
import de.melody.speechpackets.Language;
import de.melody.tools.ConsoleLogger;
import de.melody.tools.Utils.Emoji;
import net.dv8tion.jda.api.Permission;
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
	private Language language = Language.ENGLISH;
	
	private Long expiretime = System.currentTimeMillis() + Constants.ENTITYEXPIRETIME;
	private Boolean needtoexport = false;
	
	private ReactionManager reactionmanager;
	private Melody melody = Melody.INSTANCE;
	private LiteSQL database = melody._database;
	
	private ConcurrentHashMap<Integer, ArrayOptionContainer> arrayoptions = new ConcurrentHashMap<Integer, ArrayOptionContainer>();
	
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
						language = Language.getLanguage(rsguild.getInt("language"));
					}		
					if(rsguild.getString("volume") != null) {
						volume = rsguild.getInt("volume");
					}
					if(rsguild.getString("djonly") != null) {
						djonly = rsguild.getBoolean("djonly");	
					}
					if(rsguild.getString("voteskip") != null) {
						voteskip = rsguild.getBoolean("voteskip");	
					}
					if(rsguild.getString("staymode") != null) {
						staymode = rsguild.getBoolean("staymode");	
					}
					if(rsguild.getString("announcesongs") != null) {
						announcesongs = rsguild.getBoolean("announcesongs");	
					}
					if(rsguild.getString("preventduplicates") != null) {
						preventduplicates = rsguild.getBoolean("preventduplicates");	
					}
					if(rsguild.getString("playtime") != null) {
						playtime = rsguild.getInt("playtime");
					}
					if(rsguild.getString("lastaudiochannel") != null) {
						lastaudiochannelid = rsguild.getLong("lastaudiochannel");
					}
					
					ResultSet rsoptions = database.onQuery("SELECT * FROM guildoptions WHERE PK_guildid = " + getGuildId());	
					while(rsoptions.next()) {
						if(rsoptions.getString("content") != null && rsoptions.getString("typeid") != null) {						
							new ArrayOptionContainer(rsoptions.getLong("content"), ArrayOptions.getFromDatabaseID(rsoptions.getInt("typeid")), true);				
						}
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
	
	public Boolean isVoteSkip() {
		renewExpireTime();
		return this.voteskip;
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
	
	public Boolean is24_7() {
		renewExpireTime();
		return this.staymode;
	}
	
	public void set24_7(Boolean new24_7) {
		this.staymode = new24_7;
		update();
	}
	
	public void setLanguage(Language newlanguage) {
		this.language = newlanguage;
		update();
	}
	
	public Language getLanguage() {
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
	
	public void setDjOnly(boolean newdjonly) {
		this.djonly = newdjonly;
		update();
	}
	
	public Boolean isMemberDJ(Member member) {
		List<Role> roles = member.getRoles();
		if(isDjOnly()) {
			for(Role role : roles) {
				for(Long djroleid : getDJRolesID()) {
					if(djroleid == role.getIdLong() || member.hasPermission(Permission.MANAGE_SERVER)) {
						return true;
					}
				}
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
	
	public boolean addDJRoleID(Long id) {
		if(!getDJRolesID().contains(id)) {
			if(guild.getRoleById(id) != null) {
				new ArrayOptionContainer(id, ArrayOptions.DJROLES);
				return true;
			}
		}
		return false;
	}
	
	public boolean removeDJRoleID(Long id) {
		for(ArrayOptionContainer aoc : getArrayOptions(ArrayOptions.DJROLES)) {
			if(aoc.getContent().equals(id)) {
				arrayoptions.get(aoc.hashCode()).delete();
				return true;
			}
		}
		return false;
	}
	
	public List<Long> getDJRolesID(){
		List<Long> list = new ArrayList<Long>();
		for(Role role : getDJRoles()) {
			list.add(role.getIdLong());
		}
		return list;
	}
	
	public List<Role> getDJRoles(){
		List<Role> list = new ArrayList<Role>();
		for(ArrayOptionContainer aoc : getArrayOptions(ArrayOptions.DJROLES)) {
			Role role;
			if((role = guild.getRoleById(aoc.getContent())) != null) {
				list.add(role);
			}
		}
		return list;
	}
	
	private List<ArrayOptionContainer> getArrayOptions(ArrayOptions option) {
		List<ArrayOptionContainer> optionarray = new ArrayList<ArrayOptionContainer>();	
		for(Entry<Integer, ArrayOptionContainer> aocentry : arrayoptions.entrySet()) {
			ArrayOptionContainer ao = aocentry.getValue();
			if(ao.getOption().equals(option) && !ao.isDeleted()) {
				optionarray.add(ao);
			}
		}
		return optionarray;
	}
	
	private void update() {
		this.needtoexport = true;
		renewExpireTime();
	}
	
	private void renewExpireTime() {
		this.expiretime = System.currentTimeMillis() + Constants.ENTITYEXPIRETIME;
	}
	
	public boolean export() {
		if (database.isConnected()) {
			arrayoptions.entrySet().forEach((aoc) -> {
				try {
					ConsoleLogger.debug("export ArrayOptionContainer", getGuildId() + " " + aoc.getValue().getContent() + " " + aoc.getValue().getOption().name());
					aoc.getValue().export();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
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
		return false;
	}
	
	private class ArrayOptionContainer {

		private boolean loaded = false;
		private boolean delete = false;
		private Long content;
		private final ArrayOptions arrayoption;

		public ArrayOptionContainer(Long content, ArrayOptions arrayoption, boolean loaded) {
			// This Constructor will be executed when the data is taken from the Database
			this.loaded = loaded;
			this.content = content;
			this.arrayoption = arrayoption;
			arrayoptions.put(this.hashCode(), this);
		}

		public ArrayOptionContainer(Long content, ArrayOptions arrayoption) {
			this.content = content;
			this.arrayoption = arrayoption;
			update();
			arrayoptions.put(this.hashCode(), this);
		}

		public void export() throws SQLException {
			if(this.loaded) {
				if (this.delete) {
					PreparedStatement ps = database.getConnection().prepareStatement("DELETE FROM guildoptions WHERE typeid = ? AND content = ? AND PK_guildid = ?");
					ps.setInt(1, this.arrayoption.getDatabaseID());
					ps.setLong(2, this.content);
					ps.setLong(3, getGuildId());
					ConsoleLogger.debug("export oc", "Delete");
					ps.executeUpdate();
					arrayoptions.remove(this.hashCode());
					return;
				}
			}else {
				if (this.delete) {
					arrayoptions.remove(this.hashCode());
					ConsoleLogger.debug("export oc", "Delete");
				}else {
					PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO guildoptions(typeid,content,PK_guildid) VALUES(?,?,?)");
					ps.setInt(1, this.arrayoption.getDatabaseID());
					ps.setLong(2, this.content);
					ps.setLong(3, getGuildId());
					ps.executeUpdate();
					ConsoleLogger.debug("export oc", "Update");
					this.loaded = true;
				}
			}
		}

		public void delete() {
			this.delete = true;
			update();
		}

		public boolean isDeleted() {
			return this.delete;
		}

		public ArrayOptions getOption() {
			return this.arrayoption;
		}

		public Long getContent() {
			return this.content;
		}
	}
	
	private enum ArrayOptions {

		DJROLES(1),
		BANNEDMEMBERS(2),
		RESTRICTEDVC(3),
		RESTRICTEDCHAT(4);
		
		final int databaseid;
		
		ArrayOptions(int databaseid){
			this.databaseid = databaseid; 
		}
	
		public int getDatabaseID() {
			return databaseid;
		}
		
		public static ArrayOptions getFromDatabaseID(int id) {
			for(ArrayOptions options : ArrayOptions.values()) {
				if(options.getDatabaseID() == id) {
					return options;
				}
			}
			return null;
		}
	}
}

package de.melody.tools.entitymanager.entitys;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vdurmont.emoji.EmojiParser;

import de.melody.core.Melody;
import de.melody.tools.ConsoleLogger;
import de.melody.tools.datamanager.files.LiteSQL;
import de.melody.tools.entitymanager.Entity;
import de.melody.tools.messenger.Language;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public final class GuildEntity extends Entity {

	private final Long guildid;

	private LiteSQL database = Melody.getDatabase();

	public GuildEntity(Long guildid) {
		this.guildid = guildid;
		if (database.isConnected()) {
			try {
				ResultSet rsguild = database.onQuery("SELECT * FROM guilds WHERE PK_guildid = " + guildid);
				if (rsguild.next()) {
					for (Options option : Options.values()) {
						if (rsguild.getString(option.name().toLowerCase()) != null) {
							switch (option) {
								case FIRSTTIMELOADED:
									option.value = rsguild.getInt(option.databasename);
									break;
								case PREFIX:
									option.value = rsguild.getString(option.databasename);
									break;
								case LANGUAGE:
									option.value = Language.getLanguage(rsguild.getInt(option.databasename));
									break;
								case VOLUME:
									option.value = rsguild.getInt(option.databasename);
									break;
								case DJONLY:
									option.value = rsguild.getBoolean(option.databasename);
									break;
								case ANNOUNCESONGS:
									option.value = rsguild.getBoolean(option.databasename);
									break;
								case VOTESKIP:
									option.value = rsguild.getBoolean(option.databasename);
									break;
								case STAYMODE:
									option.value = rsguild.getBoolean(option.databasename);
									break;
								case LASTAUDIOCHANNEL:
									option.value = rsguild.getLong(option.databasename);
									break;
							default:
								break;
							}
						}
					}
					export();
					Options.VOLUME.setValue(42);
					export();
				} else {
					Guild guild = Melody.getGuildById(guildid);
					for (TextChannel tc : guild.getTextChannels()) {
						try {
							tc.sendMessage(EmojiParser.parseToUnicode("Hello everybody, i'm "
									+ guild.getSelfMember().getAsMention() + " \n" + " `-` My prefix on "
									+ guild.getName() + " is `" + getPrefix() + "`\n"
									+ " `-` If you do not understand how I work then you can see all my commands by typing `"
									+ getPrefix() + "help`\n"
									+ " `-` When you dont like something in my config then you can easyly change it by typing `"
									+ getPrefix() + "config help`\n" + " \n"
									+ "**Otherwise have fun listening to the music from my service** " + ":notes: \n"
									+ "PS: Thanks a lot for your support, that you added me to your discord server! :sparkling_heart:")).queue();
							break;
						} catch (InsufficientPermissionException e) {}
					}
					// loads the guild in the database
					PreparedStatement ps = database.getConnection()
							.prepareStatement("INSERT INTO guilds(PK_guildid, firsttimeloaded) VALUES(?,?)");
					ps.setLong(1, guildid);
					ps.setLong(2, System.currentTimeMillis());
					ps.executeUpdate();
					update();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	//############################################################
	public Long getGuildId() {
		renewExpireTime();
		return this.guildid;
	}
	
	public Guild getGuild() {
		renewExpireTime();
		return Melody.getGuildById(guildid);
	}
	
	public Long getLastAudioChannelId() {
		renewExpireTime();
		return (Long) Options.LASTAUDIOCHANNEL.value;
	}
	
	public VoiceChannel getLastAudioChannel() {
		renewExpireTime();
		VoiceChannel channel;
		if((channel = getGuild().getVoiceChannelById(getLastAudioChannelId())) != null) {
			return channel;
		}
		return null;
	}
	
	public void setLastAudioChannelId(Long lastaudiochannelid) {
		Options.LASTAUDIOCHANNEL.setValue(lastaudiochannelid);
		update();
	}
	
	public Boolean isVoteSkip() {
		renewExpireTime();
		return (Boolean) Options.VOTESKIP.value;
	}
	
	public void setVoteSkip(Boolean value) {
		Options.VOTESKIP.setValue(value);
		update();
	}
	
	public int getVolume() {
		renewExpireTime();
		return (Integer) Options.VOLUME.value;
	}
	
	public void setVolume(int volume) {
		Options.VOLUME.setValue(volume);
		update();
	}
	
	public String getPrefix() {
		renewExpireTime();
		return (String) Options.PREFIX.value;
	}

	public void setPrefix(String prefix) {
		Options.PREFIX.setValue(prefix);
		update();
	}
	
	public Boolean is24_7() {
		renewExpireTime();
		return (Boolean) Options.STAYMODE.value;
	}
	
	public void set24_7(Boolean new24_7) {
		Options.STAYMODE.setValue(new24_7);
		update();
	}
	
	public void setLanguage(Language newlanguage) {
		Options.LANGUAGE.setValue(newlanguage.getDatabaseID());
		update();
	}
	
	public Language getLanguage() {
		renewExpireTime();
		return Language.getLanguage((Integer) Options.LANGUAGE.value);
	}
	
	public Boolean canAnnounceSongs() {
		renewExpireTime();
		return (Boolean) Options.ANNOUNCESONGS.value;
	}
	
	public void setAnnounceSongs(Boolean newannouncesongs) {
		Options.ANNOUNCESONGS.setValue(newannouncesongs);
		update();
	}
	
	public Boolean isDjOnly() {
		renewExpireTime();
		return (Boolean) Options.DJONLY.value;
	}
	
	public void setDjOnly(boolean newdjonly) {
		Options.DJONLY.setValue(newdjonly);
		update();
	}
	
	//############################################################


	public boolean export() {
		if (database.isConnected()) {
			final String rawps = "UPDATE guilds SET %content% WHERE PK_guildid = ?";
			String rawoptions = "";
			final List<Options> exportoptions = new ArrayList<Options>();
			
			for(Options options : Options.values()) {
				if(options.needtoexport && options.canbeexported) {
					exportoptions.add(options);
					if(exportoptions.size() == 1) {
						rawoptions = rawoptions + options.databasename + " = ?";
					}else {
						rawoptions = rawoptions + ", " + options.databasename + " = ?";
					}
				}
			}
			if(!exportoptions.isEmpty()) {
				
				try {
					PreparedStatement ps = database.getConnection().prepareStatement(rawps.replace("%content%", rawoptions));
					int ioption = 1;
					for(Options option : exportoptions) {
						if(option.value instanceof Integer) {
							ps.setInt(ioption, (int) option.value);
						}
						else if(option.value instanceof String) {
							ps.setString(ioption, (String) option.value);
						}
						else if(option.value instanceof Boolean) {
							ps.setBoolean(ioption, (Boolean) option.value);
						}
						else if(option.value instanceof Long) {
							ps.setLong(ioption, (Long) option.value);
						}
						ioption++;
					}
					
					ps.setLong(exportoptions.size() + 1, guildid);
					ps.executeUpdate();
					ConsoleLogger.debug("export guildentity ID:"+ guildid, rawps.replace("%content%", rawoptions));		
						
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			setNeedToExport(false);
		}
		return false;
	}
	
	private enum Options {
		FIRSTTIMELOADED(System.currentTimeMillis(), "firsttimeloaded", false, false),
		PREFIX(Melody.getConfig()._defaultprefix, "prefix", false, true),
		LANGUAGE(Language.ENGLISH.getDatabaseID(), "language", false, true),
		VOLUME(50, "volume", true, true),
		DJONLY(false, "djonly", false, true),
		VOTESKIP(false, "voteskip", false, true),
		STAYMODE(false, "staymode", false, true),
		ANNOUNCESONGS(true, "announcesongs", false, true),
		PREVENTDUPLICATES(false, "prefentduplicates", false, true),
		LASTAUDIOCHANNEL(0l, "lastaudiochannel", false, true);

		Object value;
		final String databasename;
		boolean needtoexport;
		final boolean canbeexported;
		
		Options(Object value, String databasename, boolean needtoexport, boolean canbeexported) {
			this.value = value;
			this.databasename = databasename;
			this.needtoexport = needtoexport;
			this.canbeexported = canbeexported;
		}
		
		public void setValue(Object value) {
			needtoexport = true;
			this.value = value;
		}
	}

	/*
	 * public enum GuildOptions{ VOLUME("volume"), PREFIX("prefix"),
	 * LASTAUDIOCHANNEL("lastaudiochannel"), PLAYTIME("playtime");
	 * 
	 * final String databasename;
	 * 
	 * GuildOptions(String databasename){ this.databasename = databasename; }
	 * 
	 * public String getDatabaseName() { return databasename; } }
	 */
}

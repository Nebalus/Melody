package de.nebalus.dcbots.melody.tools.entitymanager.entitys;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vdurmont.emoji.EmojiParser;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.tools.ConsoleLogger;
import de.nebalus.dcbots.melody.tools.datamanager.files.LiteSQL;
import de.nebalus.dcbots.melody.tools.entitymanager.Entity;
import de.nebalus.dcbots.melody.tools.messenger.Language;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public final class GuildEntity extends Entity {

	private final Long guildid;

	private LiteSQL database = Melody.getDatabase();
	
	private short cmdrequest = 0;
	private Long ratelimitend = 0l;
	public boolean ratelimitmsgsend = false;

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
									setValue(option, rsguild.getInt(option.databasename));
									break;
								case LANGUAGE:
									setValue(option, Language.getLanguage(rsguild.getInt(option.databasename)));
									break;
								case VOLUME:
									setValue(option, rsguild.getInt(option.databasename));
									break;
								case DJONLY:
									setValue(option, rsguild.getBoolean(option.databasename));
									break;
								case ANNOUNCESONGS:
									setValue(option, rsguild.getBoolean(option.databasename));
									break;
								case VOTESKIP:
									setValue(option, rsguild.getBoolean(option.databasename));
									break;
								case STAYMODE:
									setValue(option, rsguild.getBoolean(option.databasename));
									break;
								case LASTAUDIOCHANNEL:
									setValue(option, rsguild.getLong(option.databasename));
									break;
							default:
								break;
							}
						}
					}
				} else {
					Guild guild = Melody.getGuildById(guildid);
					for (TextChannel tc : guild.getTextChannels()) {
						try {
							tc.sendMessage(EmojiParser.parseToUnicode("Hello everybody, i'm "
									+ guild.getSelfMember().getAsMention() + " \n" + " `-` My prefix on "
									+ guild.getName() + " is `"+Settings.CMD_PREFIX+"`\n"
									+ " `-` If you do not understand how I work then you can see all my commands by typing `"
									+ Settings.CMD_PREFIX+"help`\n"
									+ " `-` When you dont like something in my config then you can easyly change it by typing `"
									+ Settings.CMD_PREFIX+"config help`\n" + " \n"
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
	
	public boolean isRateLimited() {
		if(System.currentTimeMillis() < ratelimitend) {
			if(cmdrequest >= Melody.getConfig()._ratelimitmaxrequests) {
				return true;
			}
		}
		return false;
	}
	
	public void addRateRequest() {
		if(System.currentTimeMillis() > ratelimitend) {
			ratelimitend = System.currentTimeMillis() + Melody.getConfig()._ratelimititerationduration;
			cmdrequest = 1;
			ratelimitmsgsend = false;
		}else {
			cmdrequest++;
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
		return (Long) getValue(Options.LASTAUDIOCHANNEL);
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
		setValue(Options.LASTAUDIOCHANNEL, lastaudiochannelid);
		update();
	}
	
	public Boolean isVoteSkip() {
		renewExpireTime();
		return (Boolean) getValue(Options.VOTESKIP);
	}
	
	public void setVoteSkip(Boolean value) {
		setValue(Options.VOTESKIP, value);
		update();
	}
	
	public int getVolume() {
		renewExpireTime();
		return (Integer) getValue(Options.VOLUME);
	}
	
	public void setVolume(int volume) {
		ConsoleLogger.debug(getValue(Options.VOLUME));
		
		setValue(Options.VOLUME, volume);
		
		ConsoleLogger.debug(getValue(Options.VOLUME));
		update();
	}
	
	public Boolean is24_7() {
		renewExpireTime();
		return (Boolean) getValue(Options.STAYMODE);
	}
	
	public void set24_7(Boolean new24_7) {
		setValue(Options.STAYMODE, new24_7);
		update();
	}
	
	public void setLanguage(Language newlanguage) {
		setValue(Options.LANGUAGE, newlanguage.getDatabaseID());
		update();
	}
	
	public Language getLanguage() {
		renewExpireTime();
		return Language.getLanguage((Integer) getValue(Options.LANGUAGE));
	}

	public void setAnnounceSongs(Boolean newannouncesongs) {
		setValue(Options.ANNOUNCESONGS, newannouncesongs);
		update();
	}
	
	public Boolean canAnnounceSongs() {
		renewExpireTime();
		return (Boolean) getValue(Options.ANNOUNCESONGS);
	}
	
	public Boolean isDjOnly() {
		renewExpireTime();
		return (Boolean) getValue(Options.DJONLY);
	}
	
	public void setDjOnly(boolean newdjonly) {
		setValue(Options.DJONLY, newdjonly);
		update();
	}
	
	//############################################################


	public boolean export() {
		if (database.isConnected()) {
			final String rawps = "UPDATE guilds SET %CONTENT% WHERE PK_guildid = ?";
			String rawoptions = "";
			final List<Options> exportoptions = new ArrayList<Options>();
			
			for(Options options : Options.values()) {
				if(getOptionContainer(options).needtoexport && options.canbeexported) {
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
					PreparedStatement ps = database.getConnection().prepareStatement(rawps.replace("%CONTENT%", rawoptions));
					int ioption = 1;
					for(Options option : exportoptions) {
						Object ovalue = getValue(option);
						
						if(ovalue instanceof Integer) {
							ps.setInt(ioption, (int) ovalue);
							getOptionContainer(option).needtoexport = false;
						}
						else if(ovalue instanceof String) {
							ps.setString(ioption, (String) ovalue);
							getOptionContainer(option).needtoexport = false;
						}
						else if(ovalue instanceof Boolean) {
							ps.setBoolean(ioption, (Boolean) ovalue);
							getOptionContainer(option).needtoexport = false;
						}
						else if(ovalue instanceof Long) {
							ps.setLong(ioption, (Long) ovalue);
							getOptionContainer(option).needtoexport = false;
						}
						ioption++;
					}
					
					ps.setLong(exportoptions.size() + 1, guildid);
					ps.executeUpdate();
					ConsoleLogger.debug("export guildentity ID:"+ guildid, rawps.replace("%CONTENT%", rawoptions));		
						
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
		FIRSTTIMELOADED(System.currentTimeMillis(), "firsttimeloaded", false),
		LANGUAGE(Language.ENGLISH.getDatabaseID(), "language", true),
		VOLUME(50, "volume", true),
		DJONLY(false, "djonly", true),
		VOTESKIP(false, "voteskip", true),
		STAYMODE(false, "staymode", true),
		ANNOUNCESONGS(true, "announcesongs", true),
		PREVENTDUPLICATES(false, "prefentduplicates", true),
		LASTAUDIOCHANNEL(0l, "lastaudiochannel", true);

		final Object defaultvalue;
		final String databasename;
		final boolean canbeexported;
		
		Options(Object defaultvalue, String databasename, boolean canbeexported) {
			this.defaultvalue = defaultvalue;
			this.databasename = databasename;
			this.canbeexported = canbeexported;
		}
	}

	private HashMap<Options, OptionContainer> varoptions = new HashMap<Options, OptionContainer>();
	
	private void setValue(Options option, Object value) {
		if(varoptions.containsKey(option)) {
			OptionContainer oc = varoptions.get(option);
			oc.updateValue(value);
		}else {
			OptionContainer oc = new OptionContainer(option, value);
			varoptions.put(option, oc);
		}
	}
	
	private Object getValue(Options option) {
		if(varoptions.containsKey(option)) {
			OptionContainer oc = varoptions.get(option);
			if(oc.getValue() != null) {
				return oc.getValue();
			}else {
				oc.updateValue(option.defaultvalue);
			}
		}else {
			varoptions.put(option, new OptionContainer(option, option.defaultvalue));
		}
		return option.defaultvalue;
	}
	
	private OptionContainer getOptionContainer(Options option) {
		if(varoptions.containsKey(option)) {
			OptionContainer oc = varoptions.get(option);
			return oc;
		}else {
			OptionContainer oc = new OptionContainer(option, option.defaultvalue);
			varoptions.put(option, oc);
			return oc;
		}
	}
	
	private class OptionContainer{
		
		public Object value;
		public boolean needtoexport = false;
		final public Options option;
		
		public OptionContainer(Options option, Object value) {
			this.value = value;
			this.option = option;
		}
		
		public void updateValue(Object value) {
			needtoexport = true;
			this.value = value;
		}
		
		public Object getValue() {
			return this.value;
		}
		
		@SuppressWarnings("unused")
		public Options getOption() {
			return option;
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

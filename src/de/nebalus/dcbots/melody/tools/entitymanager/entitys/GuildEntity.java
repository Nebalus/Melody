package de.nebalus.dcbots.melody.tools.entitymanager.entitys;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vdurmont.emoji.EmojiParser;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.tools.ConsoleLogger;
import de.nebalus.dcbots.melody.tools.datamanager.files.LiteSQL;
import de.nebalus.dcbots.melody.tools.entitymanager.DatabaseValueContainer;
import de.nebalus.dcbots.melody.tools.entitymanager.Entity;
import de.nebalus.dcbots.melody.tools.messenger.Language;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public final class GuildEntity extends Entity {
	
	private final Long guildid;

	private final LiteSQL database = Melody.getDatabase();
		
	//This is for the command Ratelimiting 
	private short cmdrequest = 0;
	private Long ratelimitend = 0l;
	public boolean ratelimitmsgsend = false;

	public GuildEntity(Long guildid) 
	{
		this.guildid = guildid;
		
		if (database.isConnected()) 
		{
			try 
			{
				
				for (GuildEntityDBOptions option : GuildEntityDBOptions.values()) 
				{
					final DatabaseValueContainer dvc = new DatabaseValueContainer(option.name(), option.canbeexported, option.defaultvalue);
					createDatabaseValueContainer(dvc);
				}
				
				ResultSet rsguild = database.onQuery("SELECT * FROM guilds WHERE PK_guildid = " + guildid);
				if (rsguild.next()) 
				{
					for (GuildEntityDBOptions option : GuildEntityDBOptions.values()) 
					{
						final DatabaseValueContainer dvc = getDatabaseValueContainer(option.name());
						final String databasename = option.databasename;	
						
						if (rsguild.getString(databasename) != null) 
						{
							switch (option) {
								case FIRSTTIMELOADED:
									dvc.updateValue(rsguild.getLong(databasename), true);
									break;
								case LASTTIMELOADED:
									dvc.updateValue(rsguild.getLong(databasename), true);
									break;
								case LANGUAGE:
									dvc.updateValue(Language.getLanguage(rsguild.getInt(databasename)).getDatabaseID(), true);
									break;
								case VOLUME:
									dvc.updateValue(rsguild.getInt(databasename), true);
									break;
								case DJONLY:
									dvc.updateValue(rsguild.getBoolean(databasename), true);
									break;
								case ANNOUNCESONGS:
									dvc.updateValue(rsguild.getBoolean(databasename), true);
									break;
								case VOTESKIP:
									dvc.updateValue(rsguild.getBoolean(databasename), true);
									break;
								case STAYMODE:
									dvc.updateValue(rsguild.getBoolean(databasename), true);
									break;
								case LASTAUDIOCHANNEL:
									dvc.updateValue(rsguild.getLong(databasename), true);
									break;
							default:
								break;
							}
						}
					}
				} 
				else 
				{
					Guild guild = Melody.getGuildById(guildid);
					for (TextChannel tc : guild.getTextChannels()) 
					{
						try 
						{
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
						} 
						catch (InsufficientPermissionException e) 
						{}
					}
					// loads the guild in the database
					PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO guilds(PK_guildid, firsttimeloaded) VALUES(?,?)");
					ps.setLong(1, guildid);
					ps.setLong(2, System.currentTimeMillis());
					ps.executeUpdate();
				}
				
				for (GuildEntityDBOptions option : GuildEntityDBOptions.values()) 
				{
					DatabaseValueContainer dvc = getDatabaseValueContainer(option.name());
					ConsoleLogger.debug("LOADED GuildEntity ID:"+ guildid + " & VALUE -> " + option.name() + " " + dvc.getValue());
				}

			} 
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else 
		{
			throw new NullPointerException("The Database is unreachable!");
		}

	}

	private enum GuildEntityDBOptions {
		FIRSTTIMELOADED(System.currentTimeMillis(), "firsttimeloaded", false),
		LASTTIMELOADED(System.currentTimeMillis(), "lasttimeloaded", true),
		LANGUAGE(Language.ENGLISH.getDatabaseID(), "language", true),
		VOLUME(50, "volume", true),
		DJONLY(false, "djonly", true),
		VOTESKIP(false, "voteskip", true),
		STAYMODE(false, "staymode", true),
		ANNOUNCESONGS(true, "announcesongs", true),
		PREVENTDUPLICATES(false, "preventduplicates", true),
		LASTAUDIOCHANNEL(0l, "lastaudiochannel", true);

		final Object defaultvalue;
		final String databasename;
		final boolean canbeexported;
		
		GuildEntityDBOptions(Object defaultvalue, String databasename, boolean canbeexported) {
			this.defaultvalue = defaultvalue;
			this.databasename = databasename;
			this.canbeexported = canbeexported;
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
		return (Long) getDatabaseValue(GuildEntityDBOptions.LASTAUDIOCHANNEL.name());
	}
	
	public VoiceChannel getLastAudioChannel() {
		VoiceChannel channel;
		final Long channelid = getLastAudioChannelId();
		if((channel = getGuild().getVoiceChannelById(channelid)) != null) {
			return channel;
		}
		throw new NullPointerException("The VoiceChannel ID:"+ channelid +" cannot be loaded because it has been deleted or is unavalibale!");
	}
	
	public void setLastAudioChannelId(Long lastaudiochannelid) {
		updateDatabaseValue(GuildEntityDBOptions.LASTAUDIOCHANNEL.name(), lastaudiochannelid);
	}
	
	public Boolean isVoteSkip() {
		return (Boolean) getDatabaseValue(GuildEntityDBOptions.VOTESKIP.name());
	}
	
	public void setVoteSkip(Boolean value) {
		updateDatabaseValue(GuildEntityDBOptions.VOTESKIP.name(), value);
	}
	
	public int getVolume() {
		return (Integer) getDatabaseValue(GuildEntityDBOptions.VOLUME.name());
	}
	
	public void setVolume(int volume) {		
		updateDatabaseValue(GuildEntityDBOptions.VOLUME.name(), volume);
	}
	
	public Boolean is24_7() {
		return (Boolean) getDatabaseValue(GuildEntityDBOptions.STAYMODE.name());
	}
	
	public void set24_7(Boolean new24_7) {
		updateDatabaseValue(GuildEntityDBOptions.STAYMODE.name(), new24_7);
	}
	
	public void setLanguage(Language newlanguage) {
		updateDatabaseValue(GuildEntityDBOptions.LANGUAGE.name(), newlanguage.getDatabaseID());
	}
	
	public Language getLanguage() {
		return Language.getLanguage((Integer) getDatabaseValue(GuildEntityDBOptions.LANGUAGE.name()));
	}

	public void setAnnounceSongs(Boolean newannouncesongs) {
		updateDatabaseValue(GuildEntityDBOptions.ANNOUNCESONGS.name(), newannouncesongs);
	}
	
	public Boolean canAnnounceSongs() {
		
		return (Boolean) getDatabaseValue(GuildEntityDBOptions.ANNOUNCESONGS.name());
	}
	
	public Boolean isDjOnly() {
		return (Boolean) getDatabaseValue(GuildEntityDBOptions.DJONLY.name());
	}
	
	public void setDjOnly(boolean newdjonly) {
		updateDatabaseValue(GuildEntityDBOptions.DJONLY.name(), newdjonly);
	}
	
	//############################################################


	public boolean export() 
	{
		if (database.isConnected()) 
		{
			final String rawps = "UPDATE guilds SET %CONTENT% WHERE PK_guildid = ?";
			String rawoptions = "";
			final List<GuildEntityDBOptions> exportoptions = new ArrayList<GuildEntityDBOptions>();
			
			for(GuildEntityDBOptions options : GuildEntityDBOptions.values()) 
			{
				if(getDatabaseValueContainer(options.name()).needToExport()) 
				{
					exportoptions.add(options);
					if(exportoptions.size() == 1) 
					{
						rawoptions = rawoptions + options.databasename + " = ?";
					}
					else 
					{
						rawoptions = rawoptions + ", " + options.databasename + " = ?";
					}
				}
			}
			
			setNeedToExport(false);
			
			if(!exportoptions.isEmpty()) 
			{
				try 
				{
					PreparedStatement ps = database.getConnection().prepareStatement(rawps.replace("%CONTENT%", rawoptions));
					int ioption = 1;
					for(GuildEntityDBOptions option : exportoptions) 
					{
						DatabaseValueContainer dvc = getDatabaseValueContainer(option.name());
						dvc.exportValueToDatabaseRequest(ps, ioption);
						ioption++;
					}
					
					ps.setLong(exportoptions.size() + 1, guildid);
					ps.executeUpdate();
					ConsoleLogger.debug("export guildentity ID:"+ guildid, rawps.replace("%CONTENT%", rawoptions));		
						
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
					return false;
				}
				return true;
			}
		}
		return false;
	}
}

package de.melody.core;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.melody.listeners.CommandListener;
import de.melody.listeners.GuildJoinListener;
import de.melody.tools.ConsoleLogger;
import de.melody.tools.cmdbuilder.CommandManager;
import de.melody.tools.datamanager.DataManager;
import de.melody.tools.datamanager.files.Config;
import de.melody.tools.datamanager.files.LiteSQL;
import de.melody.tools.entitymanager.EntityManager;
import de.melody.tools.messenger.MessageFormatter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public final class Melody {
	
	public static Melody INSTANCE;
	public final AudioPlayerManager audioPlayerMan;
	public final ShardManager shardMan;
	public final DataManager dataMan;
	public final EntityManager entityMan;
	public final CommandManager cmdMan;
	public final MessageFormatter messageformatter;
	
	public final Long startupmillis; 
	
	public static void main(String[] args) {
		try {
			Melody.INSTANCE = new Melody();
		} catch (Exception e) {
			e.printStackTrace();
			Melody.INSTANCE.safeShutdown();
		}
	}
	
	private Melody() throws Exception {
		this.startupmillis = System.currentTimeMillis();
		
		Melody.INSTANCE = this;
		
		this.dataMan = new DataManager();
		this.messageformatter = new MessageFormatter();
		
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(dataMan.getConfig()._bottoken);
		builder.addEventListeners(new CommandListener(), new GuildJoinListener());
		builder.setActivity(Activity.playing("booting myself..."));

		this.audioPlayerMan = new DefaultAudioPlayerManager();
		this.entityMan = new EntityManager();
		
		this.shardMan = builder.build();
		for(JDA jda : this.shardMan.getShards()) {
			jda.awaitReady();
		}
		
		this.cmdMan = new CommandManager(this);
		
		AudioSourceManagers.registerRemoteSources(audioPlayerMan);
		AudioSourceManagers.registerLocalSource(audioPlayerMan);
		audioPlayerMan.getConfiguration().setFilterHotSwapEnabled(true);
		
		ConsoleLogger.info(Constants.BUILDNAME + " is succesfully loaded ("+(System.currentTimeMillis()-startupmillis)+"ms)");
	}

	public void safeShutdown() {
		//exporttodatabase();
		if(dataMan != null) {
			dataMan.getDatabase().disconnect();		
		}
		if(shardMan != null) {
			shardMan.setStatus(OnlineStatus.OFFLINE);
			shardMan.shutdown();
		}
		ConsoleLogger.info(Constants.BUILDNAME+" offline!");
		System.exit(0);
	}
	
	public static void restart() {
		
	}
	
	public static String formatMessage(Guild guild, String key, Object... args) {
		if(INSTANCE.messageformatter != null) {
			return INSTANCE.messageformatter.format(guild, key, args);
		}else {
			throw new NullPointerException("The MessageFormater is not loaded!");
		}
	}
	
	public static LiteSQL getDatabase() {
		if(INSTANCE.dataMan != null && INSTANCE.dataMan.getDatabase() != null) {
			return INSTANCE.dataMan.getDatabase();
		}else {
			throw new NullPointerException("The Database is not loaded!");
		}
	}
	
	public static Config getConfig() {
		if(INSTANCE.dataMan != null && INSTANCE.dataMan.getConfig() != null) {
			return INSTANCE.dataMan.getConfig();
		}else {
			throw new NullPointerException("The Config is not loaded!");
		}
	}
	
	public static ShardManager getShardManager() {
		if(INSTANCE.shardMan != null) {
			return INSTANCE.shardMan;
		}else {
			throw new NullPointerException("The ShardManager is not loaded!");
		}
	}
	
	public static Guild getGuildById(Long guildid) {
		return getShardManager().getGuildById(guildid);
	}
	
	public static User getUserById(Long userid) {
		return getShardManager().getUserById(userid);
	}
}

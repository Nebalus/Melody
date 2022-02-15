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
		this.cmdMan = new CommandManager();
		
		this.shardMan = builder.build();
		for(JDA jda : this.shardMan.getShards()) {
			jda.awaitReady();
		}
		
		AudioSourceManagers.registerRemoteSources(audioPlayerMan);
		AudioSourceManagers.registerLocalSource(audioPlayerMan);
		audioPlayerMan.getConfiguration().setFilterHotSwapEnabled(true);
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
	
	public LiteSQL getDatabase() {
		return dataMan.getDatabase();
	}
	
	public Config getConfig() {
		return dataMan.getConfig();
	}
	
	public Guild getGuildById(Long guildid) {
		return shardMan.getGuildById(guildid);
	}
}

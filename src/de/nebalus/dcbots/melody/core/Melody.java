package de.nebalus.dcbots.melody.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.nebalus.dcbots.melody.listeners.CommandListener;
import de.nebalus.dcbots.melody.tools.ConsoleLogger;
import de.nebalus.dcbots.melody.tools.cmdbuilder.CommandManager;
import de.nebalus.dcbots.melody.tools.datamanager.DataManager;
import de.nebalus.dcbots.melody.tools.datamanager.files.Config;
import de.nebalus.dcbots.melody.tools.datamanager.files.LiteSQL;
import de.nebalus.dcbots.melody.tools.entitymanager.EntityManager;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.MessageFormatter;
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
	
	private Thread auto_save_thread;
	
	public static void main(String[] args) {
		try {
			INSTANCE = new Melody();
		} catch (Exception e) {
			e.printStackTrace();
			if(INSTANCE != null) {
				INSTANCE.shutdown();
			}
		}
	}
	
	private Melody() throws Exception {
		ConsoleLogger.info("Starting BOOT process for "+Constants.BUILDNAME);
		this.startupmillis = System.currentTimeMillis();
		
		Melody.INSTANCE = this;
		
		this.dataMan = new DataManager();
		this.messageformatter = new MessageFormatter();
		
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(dataMan.getConfig()._bottoken);
		builder.addEventListeners(new CommandListener());
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
		
		runThreadLoop();
		
		ConsoleLogger.info(Constants.BUILDNAME + " is succesfully loaded ("+(System.currentTimeMillis()-startupmillis)+"ms)");
	}

	private void runThreadLoop() {
		this.auto_save_thread = new Thread(() -> {		
			Long time = System.currentTimeMillis() + 150000;
			while(true) {
				if(System.currentTimeMillis() > time) {
					exporttodatabase();
					time = System.currentTimeMillis() + 150000;
				}
			}
		});
		this.auto_save_thread.setName("Auto-Saver");
		this.auto_save_thread.setPriority(Thread.MAX_PRIORITY);
		this.auto_save_thread.start();
	}
	
	private void exporttodatabase() {
		ConsoleLogger.debug("Auto-Saver", "Starting to export cache");
		try{
			boolean export = false;
			for(Entry<Long, GuildEntity> entry : entityMan.guildentity.entrySet()) {
				GuildEntity value = entry.getValue();
				if(value.getExpireTime() <= System.currentTimeMillis()) {
					entityMan.removeGuildEntity(value);
					export = true;
				}else if(value.needToExport()) {
					value.export();
					export = true;
				}
			}
			if(export) {
				ConsoleLogger.debug("Auto-Saver", "Export ended sucessfully");
			}else {
				ConsoleLogger.debug("Auto-Saver", "There is nothing to export to the database");
			}
		}catch(Exception e) {
			ConsoleLogger.warning("Thread", "The current action from the Auto-Saver has been aborted");
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
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
	
	public void restart() {
		try {
			ConsoleLogger.info("Restarting "+Constants.BUILDNAME + " please wait!");
			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			File currentJar = new File(Melody.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			/* is it a jar file? */
			if(!currentJar.getName().endsWith(".jar"))	return;
			
			/* Build command: java -jar application.jar */
			final ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());

			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
		shutdown();
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

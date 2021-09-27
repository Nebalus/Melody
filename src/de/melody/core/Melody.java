package de.melody.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.melody.LiteSQL;
import de.melody.Secure;
import de.melody.commands.admin.ConfigCommand;
import de.melody.commands.dev.RestartCommand;
import de.melody.commands.info.InfoCommand;
import de.melody.commands.info.InviteCommand;
import de.melody.commands.info.PingCommand;
import de.melody.commands.music.BackCommand;
import de.melody.commands.music.FastforwardCommand;
import de.melody.commands.music.JoinCommand;
import de.melody.commands.music.LeaveCommand;
import de.melody.commands.music.LoopCommand;
import de.melody.commands.music.PauseCommand;
import de.melody.commands.music.PlayCommand;
import de.melody.commands.music.QueueCommand;
import de.melody.commands.music.ResumeCommand;
import de.melody.commands.music.RewindCommand;
import de.melody.commands.music.SeekCommand;
import de.melody.commands.music.ShuffleCommand;
import de.melody.commands.music.SkipCommand;
import de.melody.commands.music.StayCommand;
import de.melody.commands.music.StopCommand;
import de.melody.commands.music.TrackinfoCommand;
import de.melody.commands.music.VolumeCommand;
import de.melody.commands.slash.PrefixCommand;
import de.melody.entities.EntityManager;
import de.melody.entities.GuildEntity;
import de.melody.entities.UserEntity;
import de.melody.listeners.CommandListener;
import de.melody.listeners.ReactListener;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.music.PlayerManager;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.SpotifyUtils;
import de.melody.utils.Utils;
import de.melody.utils.Utils.ConsoleLogger;
import de.melody.utils.Utils.Emoji;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import de.melody.utils.commandbuilder.CommandManager;

public class Melody{
	public static Melody INSTANCE;
	public ShardManager shardMan;
	private MessageFormatter messageformatter;
	private Thread loop;
	private Thread auto_save;
	public AudioPlayerManager audioPlayerManager;
	public PlayerManager playerManager;
	public EntityManager entityManager;
	public LiteSQL database;
	private CommandManager cmdManager;
	
	public SpotifyUtils spotifyutils;
	
	public int uptime; 
	public long playedmusictime;
	
	public static void main(String[] args) {		
		try {
			new Melody();
		} catch (LoginException | IllegalArgumentException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private Melody() throws LoginException, IllegalArgumentException, InterruptedException {
		final Long startupMillis = System.currentTimeMillis();
		INSTANCE = this;
		database = new LiteSQL();
		spotifyutils = new SpotifyUtils(Secure.SPOTIFY_CLIENTID, Secure.SPOTIFY_CLIENTSECRET);
		messageformatter = new MessageFormatter();
	
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(Secure.TOKEN);
		configureMemoryUsage(builder); 
	
		builder.addEventListeners(new CommandListener() , new MusicUtil(), new ReactListener());
		builder.setActivity(Activity.playing("booting myself..."));
		
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
		this.entityManager = new EntityManager();
		this.cmdManager = new CommandManager(this);
		this.shardMan = builder.build();
		
		for(JDA jda : this.shardMan.getShards()) {
			jda.awaitReady();
		}
		
		cmdManager.registerCommands(new JoinCommand(), new FastforwardCommand(), new RewindCommand(), new SeekCommand(),
				new PlayCommand(), new VolumeCommand(), new PauseCommand(), new ResumeCommand(),
				new StopCommand(), new LeaveCommand(), new TrackinfoCommand(), new QueueCommand(), new SkipCommand(),
				new InfoCommand(), new PingCommand(), new ConfigCommand(), new InviteCommand(), new ShuffleCommand(),
				new LoopCommand(), new StayCommand(), new BackCommand(), new PrefixCommand(), new RestartCommand());
		
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		AudioSourceManagers.registerLocalSource(audioPlayerManager);
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);
		
		uptime = 0;
		Utils.loadSystemData(this);
		
		runLoop();
		//shutdown();
		ConsoleLogger.info("Bot", "Melody online! ("+(System.currentTimeMillis() - startupMillis)+"ms)");
	}
	
	
	public void runLoop() {
		this.loop = new Thread(() -> {	
			Long time = System.currentTimeMillis() + 1000;
			while(true) {		
				if(System.currentTimeMillis() > time) {
					try{
						time = System.currentTimeMillis()+1000;
						MusicUtil.onRefreshAutoDisabler(shardMan);
						if(database.isConnected()) {
							scanGuilds();	
						}
						spotifyutils.update();
						onStatusUpdate();
						uptime++;
						onCaculatingPlayedMusik();
					}catch(Exception e){
						ConsoleLogger.warning("Thread", "The current action from the LoopThread has been aborted");
						e.printStackTrace();
					}
				}
			}
		});
		this.loop.setName("LoopThread");
		this.loop.start();
		
		this.auto_save = new Thread(() -> {		
			Long time = System.currentTimeMillis() + 150000;
			while(true) {
				if(System.currentTimeMillis() > time) {
					ConsoleLogger.info("Auto-Saver", "Starting to export cache");
					try{
						time = System.currentTimeMillis()+ 150000;
						boolean export = false;
						for(Entry<Long, GuildEntity> entry : entityManager.guildentity.entrySet()) {
							GuildEntity value = entry.getValue();
							if(value.getExpireTime() <= System.currentTimeMillis()) {
								entityManager.removeGuildEntity(value);
								export = true;
							}else if(value.getNeedToExport()) {
								value.export();
								export = true;
							}
						}
						for(Entry<Long, UserEntity> entry : entityManager.userentity.entrySet()) {
							UserEntity value = entry.getValue();
							if(value.getExpireTime() <= System.currentTimeMillis()) {
								entityManager.removeUserEntity(value);
								export = true;
							}else if(value.getNeedToExport()) {
								value.export();
								export = true;
							}
						}
						Utils.saveSystemData(this);
						if(export) {
							ConsoleLogger.info("Auto-Saver", "Export ended sucessfully");
						}else {
							ConsoleLogger.info("Auto-Saver", "There is nothing to export to the database");
						}
					}catch(Exception e){
						ConsoleLogger.warning("Thread", "The current action from the Auto-Saver has been aborted");
						e.printStackTrace();
					}
				}
			}
		});
		this.auto_save.setName("Auto-Saver");
		this.auto_save.setPriority(Thread.MAX_PRIORITY);
		this.auto_save.start();
	}
	
	public void onCaculatingPlayedMusik() {
		for(Guild g : shardMan.getGuilds()) {
			GuildVoiceState state;
			if(g.getSelfMember() != null && (state = g.getSelfMember().getVoiceState()) != null) {
				VoiceChannel vc;
				if((vc = state.getChannel()) != null) {
					MusicController controller = playerManager.getController(vc.getGuild().getIdLong());	
					AudioPlayer player = controller.getPlayer();
					if(player.getPlayingTrack() != null && !player.isPaused()) {
						for(Member m : vc.getMembers()) {
							if(!m.getUser().isBot()) {
								
								//onCaculatingHeardMusic
								UserEntity ue = entityManager.getUserEntity(m.getUser());
								Long listendata = ue.getHeardTime();
								listendata++;
								ue.setHeardTime(listendata);
								playedmusictime++;
								//
							}
						}
					}
				}
			}
		}
	}
	
	int nextStatusUpdate = 10;
	public void onStatusUpdate() {
		if(nextStatusUpdate <= 0) {
			Random rand = new Random();
			int i = rand.nextInt(3);
			shardMan.getShards().forEach(jda ->{
				switch(i) {
				case 0:
					int musicguilds = 0;
					for(Guild g : shardMan.getGuilds()) {
						if(g.getSelfMember().getVoiceState().getChannel() != null) {
							musicguilds++;
						}
					}
					jda.getPresence().setActivity(Activity.streaming("music on " +musicguilds+" server"+(musicguilds < 1 ? "s": "") +"!","https://twitch.tv/nebalus"));
					break;
				case 1:
					jda.getPresence().setActivity(Activity.listening("m!help | "+Constants.BUILDVERSION));
					break;
				case 2:
					jda.getPresence().setActivity(Activity.listening("@"+jda.getSelfUser().getName()));
					break;
				}
			});
			nextStatusUpdate = 15;
		}else {
			nextStatusUpdate--;
		}
	}
	
	Integer guildScannerCooldown = 0;
	ArrayList<Long> guildCache = new ArrayList<>();
	public void scanGuilds() {
		if(guildScannerCooldown < 20) {
			for(Guild g : shardMan.getGuilds()) {
				if(!guildCache.contains(g.getIdLong()) && !Utils.doesGuildExist(g.getIdLong())) {		
					boolean mentioned = false;
					for(TextChannel tc : g.getTextChannels()) {
						if(!mentioned) {
							try {
								tc.sendMessage("Hello everybody, i'm "+g.getJDA().getSelfUser().getAsMention()+" "+g.getJDA().getEmoteById(Emoji.HEY_GUYS).getAsMention()+"\n"
										+ " \n"
										+ " `-` My prefix on "+g.getName()+" is `m!`\n"
										+ " `-` If you do not understand how I work then you can see all my commands by typing `m!help`\n"
										+ " `-` When you dont like something in my config then you can easyly change it by typing `m!config help`\n"
										+ " \n"
										+ "**Otherwise have fun listening to the music from my service** "+ Emoji.MUSIC_NOTE+" \n"
										+ "PS: Thanks a lot for your support, that you added me to your discord server! "+g.getJDA().getEmoteById(Emoji.ANIMATED_HEARTS).getAsMention()).queue();
								mentioned = true;
								//loads the guild in the database
								entityManager.getGuildEntity(g).setMusicChannelId(tc.getIdLong());
								guildCache.add(tc.getIdLong());
							}catch(InsufficientPermissionException e) {}
						}
					}
				}else {
					guildCache.add(g.getIdLong());
				}	
			}
			guildScannerCooldown = 0;
		}
		guildScannerCooldown++;
	}
	
	public void shutdown() {
		new Thread(() -> {		
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while((line = reader.readLine()) != null) {
					if(line.equalsIgnoreCase("stop")) {
						if(shardMan != null) {
							database.disconnect();					
							shardMan.setStatus(OnlineStatus.OFFLINE);
							shardMan.shutdown();
							ConsoleLogger.info("Bot", "Melody offline!");
						}
						if(loop != null) {
							loop.interrupt();
						}	
						if(auto_save != null) {
							auto_save.interrupt();
						}
						reader.close();
						break;
					}else {
						ConsoleLogger.info("Command", "Use \"Stop\" to shutdown!");
					}
				}
			}catch(IOException e) {}			
		}).start();	
	}
	
	private void configureMemoryUsage(DefaultShardManagerBuilder builder) {
	    // Disable cache for member activities (streaming/games/spotify)
	    builder.disableCache(CacheFlag.ACTIVITY);

	    // Only cache members who are either in a voice channel or owner of the guild
	    builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.ONLINE));

	    // Disable member chunking on startup
	    builder.setChunkingFilter(ChunkingFilter.NONE);
	    
	    // Enable presence updates 
	    builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
	    builder.setLargeThreshold(50);
	}
	
	public CommandManager getCmdMan() {
		return this.cmdManager;
	}

    public MessageFormatter getMessageFormatter() {
		return this.messageformatter;
    }
    
    public LiteSQL getDatabase() {
    	return this.database;
    }
    
    public EntityManager getEntityManager() {
    	return this.entityManager;
    }

	public ShardManager getShardManager() {
		return this.shardMan;
	}
	
	public static String getCurrentJarPath() {
	 	String path = getJarPath();
	 	if(path.endsWith(".jar")) {
	    	return path.substring(0, path.lastIndexOf("/"));
	 	}
	 	return path;
	}
	    
	public static String getJarPath() {
		final CodeSource source = Melody.INSTANCE.getClass().getProtectionDomain().getCodeSource();
		if (source != null) {
	    	return source.getLocation().getPath().replaceAll("%20", " ");
		}
	return null;
	}
}

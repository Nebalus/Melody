package de.melody;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.CodeSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Map.Entry;
import java.util.Random;
import javax.security.auth.login.LoginException;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.melody.commands.types.SlashCommand;
import de.melody.entities.EntityManager;
import de.melody.entities.GuildEntity;
import de.melody.entities.UserEntity;
import de.melody.listeners.CommandListener;
import de.melody.listeners.ReactListener;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.music.PlayerManager;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emojis;
import de.melody.utils.SpotifyAPI;
import de.melody.utils.Utils;
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
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Melody {
	public static Melody INSTANCE;
	public ShardManager shardMan;
	private CommandManager cmdMan;
	private MessageFormatter messageformatter;
	private Thread loop;
	private Thread auto_save;
	public AudioPlayerManager audioPlayerManager;
	public PlayerManager playerManager;
	public EntityManager entityManager;
	public LiteSQL database;
	
	public SpotifyAPI spotifyapi;
	
	public int uptime; 
	public long startuptime; 
	public final String version = "Alpha 0.6.0";
	public static final String name = "Melody";
	public static final String commandprefix = "/";
	public static final Long expiretime = 1000l*60l*60l;
	public static final Color HEXEmbeld = Color.decode("#32a87f");
	public static final Color HEXEmbeldError = Color.RED;
	public static final Color HEXEmbeldQueue = Color.decode("#212121"); 
	
	//ODAxODU2Njc4MDYzODk4NjQ0.YAmxOQ.98iS_fE3XICIgRx489YC14f6iAc
	public final String Token = "NzEyNzI4MDY0MDAxMzEwODM2.XsVxvA.BaMpWrE4aWoTG2467QfG7WesxH0";
	//NzEyNzI4MDY0MDAxMzEwODM2.XsVxvA.BaMpWrE4aWoTG2467QfG7WesxH0
	
	public static void main(String[] args) {		
		try {
			new Melody();
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	public Melody() throws LoginException, IllegalArgumentException {
		
		    String fonts[] = 
		      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		    for ( int i = 0; i < fonts.length; i++ ){
		      System.out.println(fonts[i]);
		    }
		Long startupMillis;
		startupMillis = System.currentTimeMillis();
		INSTANCE = this;
		database = new LiteSQL();
		spotifyapi = new SpotifyAPI("3b931823a91148bfb33844f902fc18d3", "502262203e3f46a89585eee1a9a68c4d");
		messageformatter = new MessageFormatter();
		Json.connect();
		
		MusicUtil.loadDomains();
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(Token);
		configureMemoryUsage(builder); 
	
		builder.addEventListeners(new CommandListener() , new MusicUtil(), new ReactListener());
		builder.setActivity(Activity.playing("booting myself..."));
		
		
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
		this.entityManager = new EntityManager();
		this.cmdMan = new CommandManager();
		
		this.shardMan = builder.build();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		AudioSourceManagers.registerLocalSource(audioPlayerManager);
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);
		
		/*
		for(JDA jda : shardMan.getShards()) {
			CommandListUpdateAction commands = jda.updateCommands();
			for(Entry<String, SlashCommand> entry : cmdMan.getCommandData().entrySet()) {
				commands.addCommands(entry.getValue().getCommandData());
				System.out.println(entry.getValue().getCommandData().getName());
			}
			commands.queue();
		}
		*/
		
		
		startupMillis = System.currentTimeMillis() - startupMillis;
		uptime = 0;
		startuptime = System.currentTimeMillis();

		
		runLoop();
		shutdown();
		ConsoleLogger.info("Bot", "Melody online! ("+startupMillis+"ms)");
	}
	
	
	public void runLoop() {
		this.loop = new Thread(() -> {
			
			Long time = System.currentTimeMillis();
			while(true) {		
					if(System.currentTimeMillis() >= time + 1000) {
						try{
						time = System.currentTimeMillis();
						MusicUtil.onRefreshAutoDisabler(shardMan);
						if(database.isConnected()) {
							scanGuilds();	
						}
						spotifyapi.update();
						onStatusUpdate();
						uptime = uptime + 1;
						Json.setTotalOnlineTime(Json.getTotalOnlineTime()+1l);
						onCaculatingPlayedMusik();
						//onCaculatingHeardMusic();
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
			Long time = System.currentTimeMillis();
			while(true) {
				if(System.currentTimeMillis() >= time + 150000) {
					ConsoleLogger.info("Auto-Saver", "Starting to export cache");
					try{
						time = System.currentTimeMillis();
						boolean export = false;
						for(Entry<Long, GuildEntity> entry : entityManager.guildentity.entrySet()) {
							GuildEntity value = entry.getValue();
							if(value.getExpireTime() <= System.currentTimeMillis()) {
								entityManager.removeGuildEntity(value);
								export = true;
							}else if(value.getNeedToExport()) {
								value.exportData();
								export = true;
							}
						}
						for(Entry<Long, UserEntity> entry : entityManager.userentity.entrySet()) {
							UserEntity value = entry.getValue();
							if(value.getExpireTime() <= System.currentTimeMillis()) {
								entityManager.removeUserEntity(value);
								export = true;
							}else if(value.getNeedToExport()) {
								value.exportData();
								export = true;
							}
						}
						if(export) {
							ConsoleLogger.info("Auto-Saver", "Export ended sucessfully");
						}else {
							ConsoleLogger.info("Auto-Saver", "There is nothing to export to the database");
						}
					}catch(ConcurrentModificationException e){
						ConsoleLogger.warning("Thread", "The current action from the Auto-Saver has been aborted");
						e.printStackTrace();
					}
				}
			}
		});
		this.auto_save.setName("Auto-Saver");
		this.auto_save.start();
		this.auto_save.setPriority(Thread.MAX_PRIORITY);
	}
	/*
	public void onCaculatingHeardMusic() {
		if(LiteSQL.isConnected()) {
			for(Guild g : shardMan.getGuilds()) {
				GuildVoiceState state;
				if(g.getSelfMember() != null) {
					if((state = g.getSelfMember().getVoiceState()) != null) {
						VoiceChannel vc;
						if((vc = state.getChannel()) != null) {
							MusicController controller = playerManager.getController(vc.getGuild().getIdLong());	
							AudioPlayer player = controller.getPlayer();
							if(player.getPlayingTrack() != null) {
								if(!player.isPaused()) {
									for(Member m : vc.getMembers()) {
										if(!m.getUser().isBot()) {
											Long listendata = Misc.getUserlistenTime(m.getIdLong());
											listendata++;
											Misc.setUserlistenTime(m.getIdLong(), listendata);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	*/
	public void onCaculatingPlayedMusik() {
		Long timeplayed = 0l;
		timeplayed = Json.getPlayedMusicTime();
		for(Guild g : shardMan.getGuilds()) {
			GuildVoiceState state;
			if(g.getSelfMember() != null && (state = g.getSelfMember().getVoiceState()) != null) {
				VoiceChannel vc;
				if((vc = state.getChannel()) != null) {
					MusicController controller = playerManager.getController(vc.getGuild().getIdLong());	
					AudioPlayer player = controller.getPlayer();
					if(player.getPlayingTrack() != null && !player.isPaused()) {
						int users = 0;
						for(Member m : vc.getMembers()) {
							if(!m.getUser().isBot()) {
								users++;
							}
						}
						if(users > 0) {
							timeplayed = timeplayed +1l;
						}
					}
				}
			}
		}
		if(timeplayed > Json.getPlayedMusicTime()) {
			Json.setPlayedMusicTime(timeplayed);
		}
	}
	
	int nextStatusUpdate = 6;
	public void onStatusUpdate() {
		if(nextStatusUpdate <= 0) {
			Random rand = new Random();
			int i = rand.nextInt(4);
			shardMan.getShards().forEach(jda ->{
				switch(i) {
				case 0:
					int musicguilds = 0;
					for(Guild g : shardMan.getGuilds()) {
						GuildVoiceState state;
						if((state = g.getSelfMember().getVoiceState()) != null) {
							if(state.getChannel() != null) {
								musicguilds++;
							}
						}
					}
					jda.getPresence().setActivity(Activity.streaming("music on " +musicguilds+" server"+(musicguilds < 1 ? "s": "") +"!","https://twitch.tv/nebalus"));
					break;
				case 1:
					jda.getPresence().setActivity(Activity.listening("/help | "+version));
					break;
				case 2:
					jda.getPresence().setActivity(Activity.listening("@"+jda.getSelfUser().getName()));
					break;
				case 3:
					jda.getPresence().setActivity(Activity.playing("a total of "+Utils.uptime(Json.getPlayedMusicTime())+ " music for "+Utils.getUserInt()+" users!"));
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
		if(guildScannerCooldown <= 5) {
			for(Guild g : shardMan.getGuilds()) {
				if(!guildCache.contains(g.getIdLong())) {
					if(!Utils.doesGuildExist(g.getIdLong())) {
						boolean mentioned = false;
						for(TextChannel tc : g.getTextChannels()) {
							if(!mentioned) {
								try {
									tc.sendMessage("Hello everybody, i'm "+g.getJDA().getSelfUser().getAsMention()+" "+g.getJDA().getEmoteById(Emojis.HEY_GUYS).getAsMention()+"\n"
											+ " \n"
											+ " `-` My prefix on "+g.getName()+" is `"+commandprefix+"`\n"
											+ " `-` If you do not understand how I work then you can see all my commands by typing `"+commandprefix+"help`\n"
											+ " `-` When you dont like something in my config then you can easyly change it by typing `"+commandprefix+"config help`\n"
											+ " \n"
											+ "**Otherwise have fun listening to the music from my service** "+ Emojis.MUSIC_NOTE+" \n"
											+ "PS: Thanks a lot for your support, that you added me to your discord server! "+g.getJDA().getEmoteById(Emojis.ANIMATED_HEARTS).getAsMention()).queue();
									mentioned = true;
									try {
										PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO general(guildid,channelid) VALUES(?,?)");
										ps.setLong(1, g.getIdLong());
										ps.setLong(2, tc.getIdLong());
										ps.executeUpdate();
										guildCache.add(g.getIdLong());
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}catch(InsufficientPermissionException e) {}
							}
						}
	
					}else {
						guildCache.add(g.getIdLong());
					}
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
	public void configureMemoryUsage(DefaultShardManagerBuilder builder) {
	    // Disable cache for member activities (streaming/games/spotify)
	    builder.disableCache(CacheFlag.ACTIVITY);

	    // Only cache members who are either in a voice channel or owner of the guild
	    builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.ONLINE));

	    // Disable member chunking on startup
	    builder.setChunkingFilter(ChunkingFilter.NONE);

	    // Disable presence updates and typing events
	    builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
	    
	    // Enable presence updates 
	    builder.enableIntents(GatewayIntent.GUILD_MEMBERS);

	    // Consider guilds with more than 50 members as "large". 
	    // Large guilds will only provide online members in their setup and thus reduce bandwidth if chunking is disabled.
	    builder.setLargeThreshold(50);
	}
	
	
	public CommandManager getCmdMan() {
		return cmdMan;
	}
	//gibt den Pfad dieser Jar-Datei zurück
    public String getCurrentJarPath() {
        String path = getJarPath();
        if(path.endsWith(".jar")) {
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path;
    }
    
    //gibt den absoluten Pfad inklusive Dateiname dieser Anwendung zurück
    public String getJarPath() {
        final CodeSource source = this.getClass().getProtectionDomain().getCodeSource();
        if (source != null) {
            return source.getLocation().getPath().replaceAll("%20", " ");
        }
        return null;
    }
    public MessageFormatter getMessageFormatter() {
		return messageformatter;
    }
    public LiteSQL getDatabase() {
    	return database;
    }
}

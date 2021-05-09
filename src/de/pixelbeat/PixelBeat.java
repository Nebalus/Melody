package de.pixelbeat;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.CodeSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import javax.security.auth.login.LoginException;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.pixelbeat.listeners.CommandListener;
import de.pixelbeat.listeners.ReactListener;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.music.PlayerManager;
import de.pixelbeat.speechpackets.MessageFormatter;
import de.pixelbeat.utils.Emojis;
import de.pixelbeat.utils.Utils;
import de.pixelbeat.entities.EntityManager;
import de.pixelbeat.entities.GuildEntity;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class PixelBeat {
	public static PixelBeat INSTANCE;
	public ShardManager shardMan;
	private CommandManager cmdMan;
	private MessageFormatter messageformatter;
	private Thread loop;
	public AudioPlayerManager audioPlayerManager;
	public PlayerManager playerManager;
	public EntityManager entityManager;
	public LiteSQL database;
	
	public int uptime; 
	public long startuptime; 
	public String version = "Alpha 0.5.1";
	public static final Long expiretime = 1000l*60l*10l;
	public static final Color HEXEmbeld = Color.decode("#32a87f");
	public static final Color HEXEmbeldError = Color.decode("#db3b9e");
	public static final Color HEXEmbeldQueue = Color.decode("#212121"); 
	
	public static void main(String[] args) {		
		try {
			new PixelBeat();
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	public PixelBeat() throws LoginException, IllegalArgumentException {
		
		    String fonts[] = 
		      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		    for ( int i = 0; i < fonts.length; i++ ){
		      System.out.println(fonts[i]);
		    }
		Long startupMillis;
		startupMillis = System.currentTimeMillis();
		INSTANCE = this;
		database = new LiteSQL();
		messageformatter = new MessageFormatter();
		Json.connect();
		
		
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
		this.entityManager = new EntityManager();
		this.cmdMan = new CommandManager();
		
	
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		AudioSourceManagers.registerLocalSource(audioPlayerManager);
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);
		
		startupMillis = System.currentTimeMillis() - startupMillis;
		uptime = 0;
		startuptime = System.currentTimeMillis();
		
		runLoop();
		shutdown();
		ConsoleLogger.info("Bot", "Pixel-Beat online! ("+startupMillis+"ms)");
	}
	
	
	public void runLoop() {
		this.loop = new Thread(() -> {
			
			Long time = System.currentTimeMillis();
			Long entitysavetime = System.currentTimeMillis();
			while(true) {
				
				if(System.currentTimeMillis() >= time + 1000) {
					time = System.currentTimeMillis();
					MusicUtil.onRefreshAutoDisabler(shardMan);
					if(database.isConnected()) {
						scanGuilds();	
					}
					onStatusUpdate();
					uptime = uptime + 1;
					Json.setTotalOnlineTime(Json.getTotalOnlineTime()+1l);
					onCaculatingPlayedMusik();
					//onCaculatingHeardMusic();
				}
				if(System.currentTimeMillis() >= entitysavetime + 150000) {
					for(Map.Entry<Long, GuildEntity> entry : entityManager.guildentity.entrySet()) {
					    GuildEntity value = entry.getValue();
					    if(value.getExpireTime() <= System.currentTimeMillis()) {
					    	entityManager.removeGuildEntity(value);
					    }
					}
				}
			}
			
		});
		this.loop.setName("LoopThread");
		this.loop.start();
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
			if(g.getSelfMember() != null) {
				if((state = g.getSelfMember().getVoiceState()) != null) {
					VoiceChannel vc;
					if((vc = state.getChannel()) != null) {
						MusicController controller = playerManager.getController(vc.getGuild().getIdLong());	
						AudioPlayer player = controller.getPlayer();
						if(player.getPlayingTrack() != null) {
							if(!player.isPaused()) {
								int users = 0;
								for(Member m : vc.getMembers()) {
									if(!m.getUser().isBot()) {
										users++;
									}
								}
								if(users != 0) {
									timeplayed = timeplayed +1l;
								}
							}
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
					if(musicguilds == 1) {
						jda.getPresence().setActivity(Activity.streaming("music on " +musicguilds+" server!","https://twitch.tv/nebalus"));
					}else {
						jda.getPresence().setActivity(Activity.streaming("music on " +musicguilds+" servers!","https://twitch.tv/nebalus"));
					}
					break;
				case 1:
					jda.getPresence().setActivity(Activity.listening("pb!help | "+version));
					break;
				case 2:
					jda.getPresence().setActivity(Activity.listening("@"+jda.getSelfUser().getName()));
					break;
				case 3:
					jda.getPresence().setActivity(Activity.playing("a total of "+Utils.uptime(Json.getPlayedMusicTime())+ " music for "+Utils.getUserInt()+" users!"));
					break;
				default:
					jda.getPresence().setActivity(Activity.watching("an error activity.json not found :("));
					break;
			}
			});
			nextStatusUpdate = 15;
		}
		else {
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
						try {
							PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO general(guildid,channelid) VALUES(?,?)");
							ps.setLong(1, g.getIdLong());
							ps.setLong(2, g.getDefaultChannel().getIdLong());
							ps.executeUpdate();
							guildCache.add(g.getIdLong());
						} catch (SQLException e) {
							e.printStackTrace();
						}
						String prefix = entityManager.getGuildEntity(g.getIdLong()).getPrefix();
						g.getDefaultChannel().sendMessage("Hello everybody, i'm "+g.getJDA().getSelfUser().getAsMention()+" "+g.getJDA().getEmoteById(Emojis.HEY_GUYS).getAsMention()+"\n"
								+ " \n"
								+ " `-` My prefix on "+g.getName()+" is `"+prefix+"`\n"
								+ " `-` If you do not understand how I work then you can see all my commands by typing `"+prefix+"help`\n"
								+ " `-` When you dont like something in my config then you can easyly change it by typing `"+prefix+"config help`\n"
								+ " `-` To change my prefix just type `"+prefix+"config prefix [newprefix]`\n"
								+ " \n"
								+ "**Otherwise have fun listening to the music from my service** "+ Emojis.MUSIC_NOTE+" \n"
								+ "PS: Thanks a lot for your support, that you added me to your discord server! "+g.getJDA().getEmoteById(Emojis.ANIMATED_HEARTS).getAsMention()).queue();
					
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
							ConsoleLogger.info("Bot", "Pixel offline!");
						}
						if(loop != null) {
							loop.interrupt();
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

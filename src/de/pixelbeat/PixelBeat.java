package de.pixelbeat;

import java.awt.Color;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import javax.security.auth.login.LoginException;
import javax.swing.JFrame;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.pixelbeat.gui.LoadingGUI;
import de.pixelbeat.gui.MainGUI;
import de.pixelbeat.listeners.CommandListener;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.music.PlayerManager;
import de.pixelbeat.utils.Emojis;
import de.pixelbeat.utils.Misc;
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
	private Thread loop;
	public AudioPlayerManager audioPlayerManager;
	public PlayerManager playerManager;
	
	public static int uptime; 
	public static long startuptime; 
	public static String version = "Alpha 0.4.1a";
	
	public static final Color HEXEmbeld = Color.decode("#32a87f");
	public static final Color HEXEmbeldError = Color.decode("#db3b9e");
	public static final Color HEXEmbeldQueue = Color.decode("#212121"); 
	
	public static Boolean hasDisplay = false;
	
	public static void main(String[] args) {	
		try {
		JFrame testfordisplay = new JFrame();
		hasDisplay = true;
		}catch(HeadlessException | ExceptionInInitializerError e) {
			ConsoleLogger.error("Bot", "No Display port found!!! :(");	
			hasDisplay = false;
		}
		if(hasDisplay) {
			new LoadingGUI();
		}
		
		try {
			new PixelBeat();
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		if(hasDisplay) {
			new MainGUI();
		}
	}
	
	public PixelBeat() throws LoginException, IllegalArgumentException {
		Long startupMillis;
		startupMillis = System.currentTimeMillis();
		INSTANCE = this;
		if(hasDisplay) {
			LoadingGUI.bar.setValue(5);
		}
		LiteSQL.connect();
		Json.connect();
		if(hasDisplay) {
			LoadingGUI.bar.setValue(15);
		}
		MusicUtil.loadDomains();//
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault("Tokan");
		configureMemoryUsage(builder);   //
		if(hasDisplay) {
			LoadingGUI.bar.setValue(50);
		}
		builder.addEventListeners(new CommandListener() , new MusicUtil());
		builder.setActivity(Activity.playing("booting myself..."));
		
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
		this.cmdMan = new CommandManager();
		if(hasDisplay) {
			LoadingGUI.bar.setValue(60);
		}
		
		this.shardMan = builder.build();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		AudioSourceManagers.registerLocalSource(audioPlayerManager);
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);
		if(hasDisplay) {	
			LoadingGUI.bar.setValue(90);
		}
		startupMillis = System.currentTimeMillis() - startupMillis;
		uptime = 0;
		startuptime = System.currentTimeMillis();
		
		runLoop();
		shutdown();
		if(hasDisplay) {
			LoadingGUI.bar.setValue(100);
		}
		ConsoleLogger.info("Bot", "Pixel-Beat online! ("+startupMillis+"ms)");
	}
	
	
	public void runLoop() {
		this.loop = new Thread(() -> {
			
			Long time = System.currentTimeMillis();
			while(true) {
				
				if(System.currentTimeMillis() >= time + 1000) {
					time = System.currentTimeMillis();
					MusicUtil.onRefreshAutoDisabler(shardMan);
					if(LiteSQL.isConnected()) {
						scanGuilds();	
					}
					onStatusUpdate();
					uptime = uptime + 1;
					Json.setTotalOnlineTime(Json.getTotalOnlineTime()+1l);
					onCaculatingPlayedMusik();
					//onCaculatingHeardMusic();
					if(hasDisplay) {
						MainGUI.updateUptime(Misc.uptime(uptime));
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
							MusicController controller = PixelBeat.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());	
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
						MusicController controller = PixelBeat.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());	
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
					jda.getPresence().setActivity(Activity.listening(Misc.getGuildPrefix(0l)+"help | "+version));
					break;
				case 2:
					jda.getPresence().setActivity(Activity.listening("@"+jda.getSelfUser().getName()));
					break;
				case 3:
					jda.getPresence().setActivity(Activity.playing("a total of "+Misc.uptime(Json.getPlayedMusicTime())+ " music for "+Misc.getUserInt()+" users!"));
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
					if(!Misc.doesGuildExist(g.getIdLong())) {
						g.getDefaultChannel().sendMessage("Hello everybody who I don't know, i'm "+g.getJDA().getSelfUser().getAsMention()+" "+g.getJDA().getEmoteById(Emojis.HEY_GUYS).getAsMention()+"\n"
								+ " \n"
								+ " `-` My prefix on "+g.getName()+" is `"+Misc.getGuildPrefix(g.getIdLong())+"`\n"
								+ " `-` If you do not understand how I work then you can see all my commands by typing `"+Misc.getGuildPrefix(g.getIdLong())+"help`\n"
								+ " `-` When you dont like something in my config then you can easyly change it by typing `"+Misc.getGuildPrefix(g.getIdLong())+"config help`\n"
								+ " `-` To change my prefix just type `"+Misc.getGuildPrefix(g.getIdLong())+"config prefix [newprefix]`\n"
								+ " \n"
								+ "**Otherwise have fun listening to the music from my service** 🎶\n"
								+ "PS: Thanks a lot for your support, that you added me to your discord server! "+g.getJDA().getEmoteById(Emojis.ANIMATED_HEARTS).getAsMention()).queue();
						try {
							PreparedStatement ps = LiteSQL.getConnection().prepareStatement("INSERT INTO general(guildid,channelid) VALUES(?,?)");
							ps.setLong(1, g.getIdLong());
							ps.setLong(2, g.getDefaultChannel().getIdLong());
							ps.executeUpdate();
							guildCache.add(g.getIdLong());
						} catch (SQLException e) {
							e.printStackTrace();
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
							LiteSQL.disconnect();					
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
			}catch(IOException e) {		
			}			
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
}

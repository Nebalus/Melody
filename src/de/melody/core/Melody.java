package de.melody.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.CodeSource;
import java.util.Map.Entry;
import java.util.Random;

import javax.security.auth.login.LoginException;

import org.apache.commons.io.FileUtils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.melody.commands.admin.CleanCommand;
import de.melody.commands.admin.ConfigCommand;
import de.melody.commands.admin.StayCommand;
import de.melody.commands.dev.ExportcmdCommand;
import de.melody.commands.dev.GetHostIPCommand;
import de.melody.commands.dev.RestartCommand;
import de.melody.commands.dev.ShutdownCommand;
import de.melody.commands.info.HelpCommand;
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
import de.melody.commands.music.StopCommand;
import de.melody.commands.music.TrackinfoCommand;
import de.melody.commands.music.VolumeCommand;
import de.melody.commands.slash.PrefixCommand;
import de.melody.datamanagment.Config;
import de.melody.datamanagment.LiteSQL;
import de.melody.entities.EntityManager;
import de.melody.entities.GuildEntity;
import de.melody.entities.UserEntity;
import de.melody.listeners.CommandListener;
import de.melody.listeners.ReactListener;
import de.melody.music.MusicController;
import de.melody.music.MusicUtil;
import de.melody.music.PlayerManager;
import de.melody.speechpackets.MessageFormatter;
import de.melody.tools.ConsoleLogger;
import de.melody.tools.SpotifyUtils;
import de.melody.tools.commandbuilder.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Melody{
	public static Melody INSTANCE;
	public ShardManager _shardMan;
	public MessageFormatter _messageformatter;
	public AudioPlayerManager _audioPlayerManager;
	public PlayerManager _playerManager;
	public EntityManager _entityManager;
	public LiteSQL _database;
	public CommandManager _cmdManager;
	public Config _config;
	public SpotifyUtils _spotifyutils;
	
	public final Long _startupmillis; 
	
	private Thread loop;
	private Thread auto_save;
	
	public static void main(String[] args) {
		try {
			new Melody();
		} catch (LoginException | IllegalArgumentException | InterruptedException | IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private Melody() throws LoginException, IllegalArgumentException, InterruptedException, IOException {
		_startupmillis = System.currentTimeMillis();
		INSTANCE = this;
		if(Constants.TEMP_DIRECTORY.exists()) {
			FileUtils.cleanDirectory(Constants.TEMP_DIRECTORY);
		}else {
			Constants.TEMP_DIRECTORY.mkdir();
		}
		_config = new Config(INSTANCE);
		//Loads the local SQL database
		_database = new LiteSQL();
		//Connects to the Spotify API to retriev track information
		_spotifyutils = new SpotifyUtils(Constants.SPOTIFY_CLIENTID, Constants.SPOTIFY_CLIENTSECRET);
		//Loads the language system
		_messageformatter = new MessageFormatter();
		
		//builds the bot 
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(_config._bottoken);
		configureMemoryUsage(builder); 
	
		builder.addEventListeners(new CommandListener() , new MusicUtil(), new ReactListener());
		builder.setActivity(Activity.playing("booting myself..."));
		
		this._audioPlayerManager = new DefaultAudioPlayerManager();
		this._playerManager = new PlayerManager();
		this._entityManager = new EntityManager();
		this._cmdManager = new CommandManager(this);
		this._shardMan = builder.build();
		
		for(JDA jda : this._shardMan.getShards()) {
			jda.awaitReady();
		}
		
		_cmdManager.registerCommands(new JoinCommand(), new FastforwardCommand(), new RewindCommand(), new SeekCommand(),
				new PlayCommand(), new VolumeCommand(), new PauseCommand(), new ResumeCommand(),
				new StopCommand(), new LeaveCommand(), new TrackinfoCommand(), new QueueCommand(), new SkipCommand(),
				new InfoCommand(), new PingCommand(), new ConfigCommand(), new InviteCommand(), new ShuffleCommand(),
				new LoopCommand(), new StayCommand(), new BackCommand(), new PrefixCommand(), new RestartCommand(),
				new CleanCommand(), new GetHostIPCommand(), new ExportcmdCommand(), new HelpCommand(), new ShutdownCommand());
		
		AudioSourceManagers.registerRemoteSources(_audioPlayerManager);
		AudioSourceManagers.registerLocalSource(_audioPlayerManager);
		_audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);
		
		
		runLoop();
		//shutdown();
		
		//Checks if a guild has 24/7 Mode on
		for(Guild guild : _shardMan.getGuilds()) {
			GuildEntity ge = _entityManager.getGuildEntity(guild);
			VoiceChannel vc;
			if(ge.is24_7() && (vc = ge.getLastAudioChannel()) != null) {
				AudioManager am = guild.getAudioManager();
				am.openAudioConnection(vc);
				//am.setSelfDeafened(true);	
			}
		}
		
		ConsoleLogger.info("Bot", "Melody online! ("+(System.currentTimeMillis() - _startupmillis)+"ms)");
	}
	
	private void runLoop() {
		
		this.loop = new Thread(() -> {	
			Long time = System.currentTimeMillis() + 1000;
			while(true) {		
				if(System.currentTimeMillis() > time) {
					try{
						time = System.currentTimeMillis()+1000;
						MusicUtil.onRefreshAutoDisabler(_shardMan);
						_spotifyutils.update();
						onStatusUpdate();
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
					exporttodatabase();
					time = System.currentTimeMillis() + 150000;
				}
			}
		});
		this.auto_save.setName("Auto-Saver");
		this.auto_save.setPriority(Thread.MAX_PRIORITY);
		this.auto_save.start();
	}
	
	private void exporttodatabase() {
		ConsoleLogger.debug("Auto-Saver", "Starting to export cache");
		try{
			boolean export = false;
			for(Entry<Long, GuildEntity> entry : _entityManager.guildentity.entrySet()) {
				GuildEntity value = entry.getValue();
				if(value.getExpireTime() <= System.currentTimeMillis()) {
					_entityManager.removeGuildEntity(value);
					export = true;
				}else if(value.getNeedToExport()) {
					value.export();
					export = true;
				}
			}
			for(Entry<Long, UserEntity> entry : _entityManager.userentity.entrySet()) {
				UserEntity value = entry.getValue();
				if(value.getExpireTime() <= System.currentTimeMillis()) {
					_entityManager.removeUserEntity(value);
					export = true;
				}else if(value.getNeedToExport()) {
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
	
	public void onCaculatingPlayedMusik() {
		for(Guild g : _shardMan.getGuilds()) {
			GuildVoiceState state;
			if(g.getSelfMember() != null && (state = g.getSelfMember().getVoiceState()) != null) {
				VoiceChannel vc;
				if((vc = state.getChannel()) != null) {
					MusicController controller = _playerManager.getController(vc.getGuild().getIdLong());	
					AudioPlayer player = controller.getPlayer();
					if(player.getPlayingTrack() != null && !player.isPaused()) {
						
						GuildEntity ge = _entityManager.getGuildEntity(g);
						int playdata = ge.getPlayTime();
						playdata++;
						ge.setPlayTime(playdata);
						ge.setLastAudioChannelId(vc.getIdLong());
						
						for(Member m : vc.getMembers()) {
							if(!m.getUser().isBot()) {
								
								//onCaculatingHeardMusic
								UserEntity ue = _entityManager.getUserEntity(m.getUser());
								int listendata = ue.getHeardTime();
								listendata++;
								ue.setHeardTime(listendata);
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
			_shardMan.getShards().forEach(jda ->{
				switch(i) {
				case 0:
					int musicguilds = 0;
					for(Guild g : _shardMan.getGuilds()) {
						if(g.getSelfMember().getVoiceState().getChannel() != null) {
							musicguilds++;
						}
					}
					jda.getPresence().setActivity(Activity.streaming("music on " +musicguilds+" server"+(musicguilds < 1 ? "s": "") +"!","https://twitch.tv/nebalus"));
					break;
				case 1:
					jda.getPresence().setActivity(Activity.watching(Constants.BUILDVERSION));
					break;
				case 2:
					jda.getPresence().setActivity(Activity.listening("@"+jda.getSelfUser().getName()));
					break;
				}
			});
			nextStatusUpdate = 30;
		}else {
			nextStatusUpdate--;
		}
	}
	
	public void safeShutdown() {
		if(loop != null) {
			loop.interrupt();
		}	
		if(auto_save != null) {
			auto_save.interrupt();
		}
		exporttodatabase();
		_database.disconnect();		
		if(_shardMan != null) {
			_shardMan.setStatus(OnlineStatus.OFFLINE);
			_shardMan.shutdown();
		}
		ConsoleLogger.info(Constants.BUILDNAME+" offline!");
		System.exit(0);
	}
	
	
	public void shutdownthread() {
		new Thread(() -> {		
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while((line = reader.readLine()) != null) {
					if(line.equalsIgnoreCase("stop")) {
						safeShutdown();
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

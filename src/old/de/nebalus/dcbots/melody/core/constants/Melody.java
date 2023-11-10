package old.de.nebalus.dcbots.melody.core.constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import old.de.nebalus.dcbots.melody.interactions.commands.admin.CleanCommand;
import old.de.nebalus.dcbots.melody.interactions.commands.admin.StaymodeCommand;
import old.de.nebalus.dcbots.melody.interactions.commands.info.FeedbackCommand;
import old.de.nebalus.dcbots.melody.interactions.commands.info.HelpCommand;
import old.de.nebalus.dcbots.melody.interactions.commands.info.InfoCommand;
import old.de.nebalus.dcbots.melody.interactions.commands.info.InviteCommand;
import old.de.nebalus.dcbots.melody.interactions.commands.info.PingCommand;
import old.de.nebalus.dcbots.melody.interactions.commands.music.JoinCommand;
import old.de.nebalus.dcbots.melody.interactions.commands.music.LoopCommand;
import old.de.nebalus.dcbots.melody.listeners.InteractionListener;
import old.de.nebalus.dcbots.melody.tools.ConsoleLogger;
import old.de.nebalus.dcbots.melody.tools.audioplayer.MusicManager;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.CommandManager;
import old.de.nebalus.dcbots.melody.tools.datamanager.DataManager;
import old.de.nebalus.dcbots.melody.tools.datamanager.files.Config;
import old.de.nebalus.dcbots.melody.tools.datamanager.files.LiteSQL;
import old.de.nebalus.dcbots.melody.tools.entitymanager.EntityManager;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.UserEntity;
import old.de.nebalus.dcbots.melody.tools.messenger.Language;
import old.de.nebalus.dcbots.melody.tools.messenger.MessageFormatter;

public final class Melody {

	public static Melody INSTANCE;

	private final AudioPlayerManager audioplayerMan;
	private final MusicManager musicMan;
	private final ShardManager shardMan;
	private final DataManager dataMan;
	private final EntityManager entityMan;
	private final CommandManager cmdMan;
	private final MessageFormatter messageformatter;

	private final Long startuptimestamp;

	private int nextStatusUpdate = 10;

	private Thread auto_save_thread;
	private Thread loop_thread;

	public static void main(String[] args) {
		try {
			INSTANCE = new Melody();
		} catch (Exception e) {
			e.printStackTrace();
			if (INSTANCE != null) {
				INSTANCE.shutdown();
			}
		}
	}

	private Melody() throws Exception {
		new Build("BETA v0.7.0", "2022-08-30");

		ConsoleLogger.info("Starting BOOT process for " + Build.NAME + " " + Build.VERSION);

		startuptimestamp = System.currentTimeMillis();

		Melody.INSTANCE = this;

		dataMan = new DataManager();
		messageformatter = new MessageFormatter();

		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(dataMan.getConfig().BOTTOKEN);
		builder.addEventListeners(new InteractionListener());
		builder.setActivity(Activity.playing("booting myself..."));

		shardMan = builder.build();
		for (JDA jda : shardMan.getShards()) {
			jda.awaitReady();
		}

		audioplayerMan = new DefaultAudioPlayerManager();
		musicMan = new MusicManager();
		entityMan = new EntityManager();

		cmdMan = new CommandManager();
		cmdMan.registerCommands(new StaymodeCommand(), new JoinCommand(), new CleanCommand(), new InfoCommand(),
				new HelpCommand(), new PingCommand(), new InviteCommand(), new LoopCommand(), new FeedbackCommand());

		AudioSourceManagers.registerRemoteSources(audioplayerMan);
		AudioSourceManagers.registerLocalSource(audioplayerMan);
		audioplayerMan.getConfiguration().setFilterHotSwapEnabled(true);

		runThreadLoop();

		ConsoleLogger.info(Build.NAME + " has been successfully loaded ("
				+ (System.currentTimeMillis() - startuptimestamp) + "ms)");
	}

	private void runThreadLoop() {
		auto_save_thread = new Thread(() -> {
			Long time = System.currentTimeMillis();
			while (true) {
				try {
					TimeUnit.MILLISECONDS.sleep(150000);
				} catch (InterruptedException e) {
				}

				if (System.currentTimeMillis() > time) {
					exportToDatabase();
					time = System.currentTimeMillis() + 125000;
				}
			}
		});
		auto_save_thread.setName("Auto-Saver");
		auto_save_thread.setPriority(Thread.MAX_PRIORITY);
		auto_save_thread.start();

		loop_thread = new Thread(() -> {

			Long time = System.currentTimeMillis();

			while (true) {
				try {
					TimeUnit.MILLISECONDS.sleep(1000);
				} catch (InterruptedException e) {
				}

				if (System.currentTimeMillis() > time) {

					updateActivityStatus();

					time = System.currentTimeMillis() + 1000;
				}
			}
		});
		loop_thread.setName("Loop-Thread");
		loop_thread.setPriority(Thread.NORM_PRIORITY);
		loop_thread.start();
	}

	private void exportToDatabase() {
		ConsoleLogger.debug("Auto-Saver", "Starting to export cache");
		try {
			if (getEntityManager().exportToDatabase()) {
				ConsoleLogger.debug("Auto-Saver", "Export ended sucessfully");
			} else {
				ConsoleLogger.debug("Auto-Saver", "There is nothing to export to the database");
			}
		} catch (Exception e) {
			ConsoleLogger.warning("Thread", "The current action from the Auto-Saver has been aborted");
			e.printStackTrace();
		}
	}

	private void updateActivityStatus() {
		if (nextStatusUpdate <= 0) {
			final Random rand = new Random();
			final int i = rand.nextInt(3);

			shardMan.getShards().forEach(jda -> {
				switch (i) {
				case 0:
					int musicguilds = 0;
					for (Guild g : shardMan.getGuilds()) {
						if (g.getSelfMember().getVoiceState().getChannel() != null) {
							musicguilds++;
						}
					}
					jda.getPresence()
							.setActivity(Activity.streaming(
									"music on " + musicguilds + " server" + (musicguilds < 1 ? "s" : "") + "!",
									"https://twitch.tv/nebalus"));
					break;
				case 1:
					jda.getPresence().setActivity(Activity.watching(Build.VERSION));
					break;
				case 2:
					jda.getPresence().setActivity(Activity.listening("@" + jda.getSelfUser().getName()));
					break;
				}
			});
			nextStatusUpdate = 30;
		} else {
			nextStatusUpdate--;
		}
	}

	public void shutdown() {
		exportToDatabase();
		if (dataMan != null) {
			dataMan.getDatabase().disconnect();
		}
		if (shardMan != null) {
			shardMan.setStatus(OnlineStatus.OFFLINE);
			shardMan.shutdown();
		}
		ConsoleLogger.info(Build.NAME + " " + Build.VERSION + " offline!");
		System.exit(0);
	}

	public void restart() {
		try {
			ConsoleLogger.info("Restarting " + Build.NAME + " " + Build.VERSION + ", please wait!");
			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			File currentJar = new File(Melody.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			/* is it a jar file? */
			if (!currentJar.getName().endsWith(".jar")) {
				return;
			}

			/* Build command: java -jar application.jar */
			final ArrayList<String> command = new ArrayList<>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());

			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		shutdown();
	}

	public static Long getStartUpTimeStamp() {
		if (INSTANCE != null) {
			return INSTANCE.startuptimestamp;
		} else {
			return System.currentTimeMillis();
		}
	}

	public static String formatMessage(Guild guild, String key, Object... args) {
		if (INSTANCE.messageformatter != null) {
			return INSTANCE.messageformatter.format(guild, key, args);
		} else {
			throw new NullPointerException("The MessageFormater is not loaded!");
		}
	}

	public static String formatMessage(Language lang, String key, Object... args) {
		if (INSTANCE.messageformatter != null) {
			return INSTANCE.messageformatter.format(lang, key, args);
		} else {
			throw new NullPointerException("The MessageFormater is not loaded!");
		}
	}

	public static LiteSQL getDatabase() {
		if (INSTANCE.dataMan != null && INSTANCE.dataMan.getDatabase() != null) {
			return INSTANCE.dataMan.getDatabase();
		} else {
			throw new NullPointerException("The Database is not loaded!");
		}
	}

	public static Config getConfig() {
		if (INSTANCE.dataMan != null && INSTANCE.dataMan.getConfig() != null) {
			return INSTANCE.dataMan.getConfig();
		} else {
			throw new NullPointerException("The Config is not loaded!");
		}
	}

	public static DataManager getDataManager() {
		if (INSTANCE.dataMan != null) {
			return INSTANCE.dataMan;
		} else {
			throw new NullPointerException("The DataManager is not loaded!");
		}
	}

	public static CommandManager getCommandManager() {
		if (INSTANCE.cmdMan != null) {
			return INSTANCE.cmdMan;
		} else {
			throw new NullPointerException("The CommandManager is not loaded!");
		}
	}

	public static MusicManager getMusicManager() {
		if (INSTANCE.musicMan != null) {
			return INSTANCE.musicMan;
		} else {
			throw new NullPointerException("The PlayerManager is not loaded!");
		}
	}

	public static AudioPlayerManager getAudioPlayerManager() {
		if (INSTANCE.audioplayerMan != null) {
			return INSTANCE.audioplayerMan;
		} else {
			throw new NullPointerException("The AudioPlayerManager is not loaded!");
		}
	}

	public static ShardManager getShardManager() {
		if (INSTANCE.shardMan != null) {
			return INSTANCE.shardMan;
		} else {
			throw new NullPointerException("The ShardManager is not loaded!");
		}
	}

	public static EntityManager getEntityManager() {
		if (INSTANCE.entityMan != null) {
			return INSTANCE.entityMan;
		} else {
			throw new NullPointerException("The EntityManager is not loaded!");
		}
	}

	public static Guild getGuildById(Long guildid) {
		return getShardManager().getGuildById(guildid);
	}

	public static GuildEntity getGuildEntityById(Long guildid) {
		return getEntityManager().getGuildEntity(guildid);
	}

	public static GuildEntity getGuildEntity(Guild guild) {
		return getEntityManager().getGuildEntity(guild);
	}

	public static User getUserById(Long userid) {
		return getShardManager().getUserById(userid);
	}

	public static UserEntity getUserEntityById(Long userid) {
		return getEntityManager().getUserEntity(userid);
	}

	public static UserEntity getUserEntity(User user) {
		return getEntityManager().getUserEntity(user);
	}
}

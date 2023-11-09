package old.de.nebalus.dcbots.melody.core.constants;

import java.awt.Color;
import java.util.List;

import old.de.nebalus.dcbots.melody.tools.messenger.Language;

public class Settings {

	public static final Long ENTITY_EXPIRE_TIME = 1000L * 60L * 60L;
	public static final Color EMBED_COLOR = Color.decode("#47EFFF");
	public static final Color ERROR_EMBED_COLOR = Color.RED;

	public static final Language DEFAULT_LANGUAGE = Language.ENGLISH;

	public static final int MUSIC_AFK_DEFAULT = 30; // In Seconds

	public static final String CMD_PREFIX = "/";

	public static final List<Long> DEVELOPER_ID_LIST = List.of(502213965485703168L);

	public static final int MAX_VOLUME = 100;

	public static final int MAX_CLEAN_MESSAGES = 200;
	public static final int DEFAULT_CLEAN_MESSAGES = 100;
}
package old.de.nebalus.dcbots.melody.tools.audioplayer;

import java.util.concurrent.ConcurrentHashMap;

import old.de.nebalus.dcbots.melody.core.constants.Melody;

public final class MusicManager {
	private ConcurrentHashMap<Long, AudioController> controller;

	public MusicManager() {
		controller = new ConcurrentHashMap<>();
	}

	public AudioController getController(long guildid) {
		if (controller.containsKey(guildid)) {
			AudioController ac = controller.get(guildid);
			return ac;
		} else {
			AudioController ac = new AudioController(Melody.getGuildById(guildid));
			controller.put(guildid, ac);
			return ac;
		}
	}

	public void clearController(long guildid) {
		if (controller.containsKey(guildid)) {
			controller.remove(guildid);
		}
	}

	public long getGuildIdByPlayerHash(int hash) {
		for (AudioController controller : this.controller.values()) {
			if (controller.getPlayer().hashCode() == hash) {
				return controller.getGuildId();
			}
		}
		return -1;
	}
}

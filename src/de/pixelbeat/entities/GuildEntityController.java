package de.pixelbeat.entities;

import de.pixelbeat.entities.reacts.ReactionManager;
import net.dv8tion.jda.api.entities.Guild;

public class GuildEntityController {

	private Guild guild;
	private ReactionManager reactionmanager;
	
	public GuildEntityController(Guild guild) {
		this.guild = guild;
		this.reactionmanager = new ReactionManager();
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public ReactionManager getReactionManager() {
		return reactionmanager;
	}
}

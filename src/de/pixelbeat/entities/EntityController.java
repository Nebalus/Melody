package de.pixelbeat.entities;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.Guild;

public class EntityController {

	private Guild guild;
	
	private HashMap<Long, QueueEmbed> queueembed = new HashMap<Long, QueueEmbed>();
	
	public EntityController(Guild guild) {
		this.guild = guild;
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public boolean addQueueEmbed(Long messageid ,QueueEmbed qe) {
		if(!queueembed.containsKey(messageid)) {
			queueembed.put(messageid, qe);
			return true;
		}
		return false;
	}
	
	public QueueEmbed getQueueEmbed(Long messageid) {
		if(queueembed.containsKey(messageid)) {
			return queueembed.get(messageid);
		}
		return null;
	}
	
	public boolean removeQueueEmbed(Long messageid) {
		if(queueembed.containsKey(messageid)) {
			queueembed.remove(messageid);
			return true;
		}
		return false;
	}
}

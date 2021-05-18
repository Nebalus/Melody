package de.melody.entities.reacts;

import java.util.HashMap;


public class ReactionManager {
	
	private HashMap<Long, QueueReaction> queuereaction = new HashMap<Long, QueueReaction>();
	private HashMap<Long, TrackReaction> trackreaction = new HashMap<Long, TrackReaction>();
	
	public boolean addReactionMessage(Long messageid, Object Class) {
		ReactionTypes raw = ((ReactionRaw) Class).getType();
		switch(raw) {
			case QUEUEREACTION:
				if(!queuereaction.containsKey(messageid)) {
					queuereaction.put(messageid, (QueueReaction) Class);
					return true;
				}
				break;
			
			case TRACKREACTION:
				if(!trackreaction.containsKey(messageid)) {
					trackreaction.put(messageid, (TrackReaction) Class);
					return true;
				}
				break;
			
			case NOTHING:
				break;
			}
		return false;
	}
	
	
	public Object getReacton(Long messageid, ReactionTypes type) {
		if(type == ReactionTypes.QUEUEREACTION && queuereaction.containsKey(messageid)) {
			return queuereaction.get(messageid);
		}else if(type == ReactionTypes.TRACKREACTION && trackreaction.containsKey(messageid)) {
			return trackreaction.get(messageid);
		}
		return null;
	}
	
	
	//Creates the interface
	public interface ReactionRaw {
		ReactionTypes getType();
	}
}


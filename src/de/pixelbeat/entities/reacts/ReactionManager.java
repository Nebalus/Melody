package de.pixelbeat.entities.reacts;

import java.util.HashMap;


public class ReactionManager {

	
	private HashMap<Long, QueueReacton> queuereaction = new HashMap<Long, QueueReacton>();
	
	
	public boolean addReactionMessage(Long messageid, Object Class) {
		ReactionTypes raw = ((ReactionRaw) Class).getType();
		switch(raw) {
			case QUEUEREACTION:
				if(!queuereaction.containsKey(messageid)) {
					queuereaction.put(messageid, (QueueReacton) Class);
					return true;
			}
		case NOTHING:
			break;
		}
		return false;
	}
	
	public QueueReacton getQueueReacton(Long messageid) {
		if(queuereaction.containsKey(messageid)) {
			return queuereaction.get(messageid);
		}
		return null;
	}
	
	public boolean removeQueueReacton(Long messageid) {
		if(queuereaction.containsKey(messageid)) {
			queuereaction.remove(messageid);
			return true;
		}
		return false;
	}
	
	//Creates the interface
	public interface ReactionRaw {
		ReactionTypes getType();
	}
}


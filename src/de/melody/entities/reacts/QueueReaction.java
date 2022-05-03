package de.melody.entities.reacts;

import de.melody.entities.reacts.ReactionManager.ReactionRaw;
import de.melody.music.Queue;

public class QueueReaction implements ReactionRaw{

	private int page;
	private Queue queue;
	
	public QueueReaction(Queue queue) {
		this.page = 1;
		this.queue = queue;
	}
	
	public int getPage() {
		return page;
	}
	
	public boolean removePage() {
		if(page > 1) {
			page--;
			return true;
		}
		return false;
	}
	
	public boolean addPage() {
		int queuepage = queue.getQueueSize()/10;
		queuepage = (float) queue.getQueueSize()/10 > queuepage ? queuepage+1 : queuepage;
		if(queuepage > page) {
			page++;
			return true;
		}
		return false;
	}
	
	public Queue getQueue() {
		return queue;
	}

	@Override
	public ReactionTypes getType() {
		return ReactionTypes.QUEUEREACTION;
	}
}

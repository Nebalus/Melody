package de.pixelbeat.entities;

import de.pixelbeat.music.Queue;

public class QueueEmbed {

	private int page;
	private Queue queue;
	
	public QueueEmbed(Queue queue) {
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
		if((((queue.getQueueSize()+1) / 10) > 0 ? ((queue.getQueueSize()+1) / 10) : 1) > page+1) {
			page++;
			return true;
		}
		return false;
	}
	
	public Queue getQueue() {
		return queue;
	}
}

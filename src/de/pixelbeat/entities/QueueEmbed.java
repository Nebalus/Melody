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
}

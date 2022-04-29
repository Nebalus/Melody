package de.nebalus.dcbots.melody.tools.audioplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.nebalus.dcbots.melody.tools.audioplayer.enums.LoopMode;

public class Queue {
	
	private final AudioController controller;
	private final ArrayList<QueuedTrack> queuelist;
	
	public Queue(AudioController controller) {
		this.controller = controller;
		this.queuelist = new ArrayList<QueuedTrack>();
	}
	
	@SuppressWarnings("incomplete-switch")
	public int next(int amount) {
		if(amount < 0) amount *= -1;
		if(amount > queuelist.size()) amount = queuelist.size();
		if(!queuelist.isEmpty()) {
			QueuedTrack qt;
			for (int i = 0; i <= amount; ++i) {
				qt = queuelist.remove(0);
				switch(controller.getLoopMode()) {
					case QUEUE:
						addTrack(qt);
						break;
					case SONG:
						controller.setLoopMode(LoopMode.NONE);
						break;
				}
			}
			if ((qt = queuelist.get(0)) != null) {
				controller.play(qt.refreshTrack());
				return amount;
			}
		}
		return 0;
	}
	
	public void addTrack(QueuedTrack queuedtrack) {
		this.queuelist.add(queuedtrack);
		if(!controller.isPlayingTrack()) {
			next(0);
		}
	}
	
	public QueuedTrack currentlyPlaying() {
		if(queuelist.get(0) != null) {
			return queuelist.get(0);	
		}
		return null;
	}
	
	public void clear() {
		queuelist.clear();	
	}
	
	public void shuffle() {
		Collections.shuffle(queuelist);
	} 
	
	public AudioController getController() {
		return controller;
	}
	
	public List<QueuedTrack> getQueuelist(){
		return queuelist;
	}
}

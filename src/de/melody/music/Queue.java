package de.melody.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Queue {
	
	public ArrayList<QueuedTrack> queuelist;
	public ArrayList<QueuedTrack> playedlist;
	private MusicController controller;
	
	public Queue(MusicController controller) {
		this.controller = controller;
		this.queuelist = new ArrayList<QueuedTrack>();
		this.playedlist = new ArrayList<QueuedTrack>();
	}
	
	public int next(int amount) {
		if(amount == 0) amount++;
		if(amount < 0) amount *= -1;
		if(amount > queuelist.size()) amount = queuelist.size()+1;
		if(!queuelist.isEmpty()) {
			for (int i = 1; i < amount;) {
				++i;
				playedlist.add(queuelist.remove(0));
			}
			if (!queuelist.isEmpty() && queuelist.get(0) != null) {
				QueuedTrack qt = queuelist.remove(0);
				playedlist.add(qt);
				controller.play(qt.getTrack().makeClone());
				return amount;
			}
		}
		return 0;
	}
	
	public int back(int amount) {
		if(amount == 0) amount++;
		if(amount < 0) amount *= -1;
		if(playedlist.size() == 1) amount = 0;
		if(amount > playedlist.size()) amount = playedlist.size()-1;
		if(!playedlist.isEmpty()) {
			ArrayList<QueuedTrack> cache = new ArrayList<QueuedTrack>();
			for (int i = 0; i < amount;) {
				++i;
				cache.add(playedlist.remove(playedlist.size()-1));
			}
			Collections.reverse(cache);
			cache.addAll(queuelist);
			queuelist = cache;
			if (!playedlist.isEmpty() && currentlyPlaying() != null) {
				controller.play(currentlyPlaying().getTrack().makeClone());		
				return amount;
			}
		}
		return 0;
	}
	
	public QueuedTrack currentlyPlaying() {
		if(playedlist.get(playedlist.size()-1) != null) {
			return playedlist.get(playedlist.size()-1);	
		}
		return null;
	}
	
	public int getQueueSize() {
		return this.queuelist.size();
	}
	
	public boolean clear() {
		if(this.queuelist.size() >= 1) {
			queuelist.clear();	
			playedlist.clear();
			return true;
		}
		return false;
	}
	
	public boolean nextexist() {
		if(this.queuelist.size() >= 1) {
			return true;
		}
		return false;
	}
	
	public void addTrackToQueue(QueuedTrack queuedtrack) {
		this.queuelist.add(queuedtrack);
		if(!controller.isPlayingTrack()) {
			QueuedTrack qt = this.queuelist.remove(0);
			playedlist.add(qt);
			controller.play(qt.getTrack().makeClone());
		}
	}
	
	public void shuffle() {
		Collections.shuffle(queuelist);
	}
	
	public MusicController getController() {
		return controller;
	}
	
	public List<QueuedTrack> getQueuelist(){
		return queuelist;
	}
}

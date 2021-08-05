package de.melody.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;

import de.melody.ConsoleLogger;
import net.dv8tion.jda.api.entities.Member;

public class Queue {
	
	private List<QueuedTrack> queuelist;
	private List<QueuedTrack> playedlist;
	private MusicController controller;
	
	private QueuedTrack currentplaying;
	
	public Queue(MusicController controller) {
		this.controller = controller;
		this.queuelist = new ArrayList<QueuedTrack>();
		this.playedlist = new ArrayList<QueuedTrack>();
	}
	
	public int next(int amount) {
		if(amount == 0) amount++;
		if(amount < 0) amount *= -1;
		if(amount > queuelist.size()) amount = queuelist.size();
		if(!queuelist.isEmpty()) {
			for (int i = 1; i < amount;) {
				++i;
				playedlist.add(queuelist.remove(0));
			}
			if (!queuelist.isEmpty() && queuelist.get(0) != null) {
				final QueuedTrack qt = queuelist.remove(0);
				currentplaying = qt;
				controller.play(qt.getTrack());
				return amount;
			}
		}
		return 0;
	}

	public QueuedTrack getCurrentPlaying() {
		return currentplaying;
	}
	
	public boolean back(int amount) {
		return false;
	
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
	
	public void addTrackToQueue(AudioTrack track, Member m) {
		this.queuelist.add(new QueuedTrack(track, m));
		if(!controller.isPlayingTrack()) {
			QueuedTrack qt = this.queuelist.remove(0);
			currentplaying = qt; 
			controller.play(qt.getTrack());
		}
	}
	
	public void shuffel() {
		Collections.shuffle(queuelist);
	}
	
	public MusicController getController() {
		return controller;
	}
	
	public List<QueuedTrack> getQueuelist(){
		return queuelist;
	}
	
}

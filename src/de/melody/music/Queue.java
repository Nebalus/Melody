package de.melody.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Member;

public class Queue {
	
	private List<QueuedTrack> queuelist;
	private List<QueuedTrack> playedlist;
	private MusicController controller;
	
	public QueuedTrack currentplaying;
	
	public Queue(MusicController controller) {
		this.controller = controller;
		this.queuelist = new ArrayList<QueuedTrack>();
		this.playedlist = new ArrayList<QueuedTrack>();
	}
	
	public boolean next() {
		if(this.queuelist.size() >= 1) {
			currentplaying = queuelist.remove(0);
			if(currentplaying != null) {
				controller.play(currentplaying.getTrack());
				return true;
			}
		}
		return false;
	}
	public boolean back() {
		if(this.playedlist.size() >= 1) {
			currentplaying = playedlist.remove(playedlist.size()-1);
			if(currentplaying != null) {
				controller.play(currentplaying.getTrack());
				return true;
			}
		}
		return false;
	}
	public int getQueueSize() {
		return this.queuelist.size();
	}
	public boolean skipTracks(int count) {
		if(this.queuelist.size() >= 1) {
			
			AudioTrack track = queuelist.remove(count).getTrack();
			
			if(track != null) {
				this.controller.getPlayer().playTrack(track);
				return true;
			}
		}
		return false;
	}
	
	public AudioTrack getTrack(int num) {
		try {
			AudioTrack track = queuelist.get(num).getTrack();
			
			if(track != null) {
				return track;
			}
			return null;
		}catch(IndexOutOfBoundsException e) {}
		return null;	
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
			next();
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

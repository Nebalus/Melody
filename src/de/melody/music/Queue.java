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
		this.setController(controller);
		this.queuelist = new ArrayList<QueuedTrack>();
		this.playedlist = new ArrayList<QueuedTrack>();
	}
	
	public boolean next() {
		if(this.queuelist.size() >= 1) {
			currentplaying = queuelist.remove(0);
			if(currentplaying != null) {
				play(currentplaying.getTrack());
				return true;
			}
		}
		return false;
	}
	public boolean play(AudioTrack at) {
		this.controller.getPlayer().playTrack(at);
		return true;
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
	
	public Member getWhoQueued(int num) {
		try {
			Member user = queuelist.get(num).getWhoQueued();
			
			if(user != null) {
				return user;
			}
		}catch(IndexOutOfBoundsException e) {}
		return null;	
	}
	
	public Boolean isPlayingTrack() {
		if(this.controller.getPlayer().getPlayingTrack() == null) {
			return false;
		}
		return true;	
	}
	
	public boolean clearAll() {
		if(this.queuelist.size() >= 1) {
			queuelist.clear();	
			return true;
		}
		return false;
	}
	
	public boolean nextExist() {
		if(this.queuelist.size() >= 1) {
			return true;
		}
		return false;
	}
	
	public void addTrackToQueue(AudioTrack track, Member m) {
		this.queuelist.add(new QueuedTrack(track, m));
		if(!isPlayingTrack()) {
			next();
		}
	}
	
	public void shuffel() {
		Collections.shuffle(queuelist);
	}
	
	public MusicController getController() {
		return controller;
	}

	public void setController(MusicController controller) {
		this.controller = controller;
	}
	
	public List<QueuedTrack> getQueuelist(){
		return queuelist;
	}
	
}

package de.pixelbeat.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.pixelbeat.ConsoleLogger;
import net.dv8tion.jda.api.entities.Member;

public class Queue {
	
	private List<AudioTrack> queuelist;
	private MusicController controller;
	
	private List<Member> whoqueued;
	private List<Long> whoqueuedid;
	
	private boolean isloop;	
	private boolean isloopqueue;
	public Queue(MusicController controller) {
		this.setController(controller);
		this.setQueuelist(new ArrayList<AudioTrack>());
		this.setWhoQueued(new ArrayList<Member>());
		this.setWhoQueuedId(new ArrayList<Long>());
		this.setLoop(false);
		this.setLoopQueue(false);
	}
	
	public boolean next() {
		if(this.queuelist.size() >= 1) {
			AudioTrack track = queuelist.remove(0);
			if(whoqueued.size() >= 2) {
				whoqueued.remove(0);
				whoqueuedid.remove(0);
			}
			if(track != null) {
				play(track);
				return true;
			}
		}
		if(this.whoqueued.size() == 1) {
			if(this.queuelist.size() == 0) {
				whoqueued.remove(0);
				whoqueuedid.remove(0);
			}
		}
		return false;
	}
	public boolean play(AudioTrack at) {
		this.controller.getPlayer().playTrack(at);
		return true;
	}
	public int getQueueSize() {
		if(this.queuelist.size() >= 1) {
			return this.queuelist.size();
		}
		return 0;
	}
	public boolean skiptracks(int count) {
		if(this.queuelist.size() >= 1) {
			
			AudioTrack track = queuelist.remove(count);
			
			if(track != null) {
				this.controller.getPlayer().playTrack(track);
				return true;
			}
		}
		return false;
	}
	public AudioTrack gettrack(int num) {
		try {
			AudioTrack track = queuelist.get(num);
			
			if(track != null) {
				return track;
			}
			return null;
		}catch(IndexOutOfBoundsException e) {
			
		}
		return null;	
	}
	public Member getuserwhoqueued(int num) {
		try {
			Member user = whoqueued.get(num);
			
			if(user != null) {
				return user;
			}
			return null;
		}catch(IndexOutOfBoundsException e) {
			
		}
		return null;	
	}
	public Boolean trackexist() {
		if(	this.controller.getPlayer().getPlayingTrack() == null) {
			return false;
		}
		return true;	
	}
	public boolean isLoop() {
		return isloop;
	}
	public boolean isLoopQueue() {
		return isloopqueue;
	}
	public void setLoopQueue(Boolean loopqueue) {
		this.isloopqueue = loopqueue;
	}
	public void setLoop(Boolean loop) {
		this.isloop = loop;
	}
	public boolean clearall() {
		if(this.queuelist.size() >= 1) {
				queuelist.clear();	
				whoqueued.clear();
				whoqueuedid.clear();
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
	public void addTrackToQueue(AudioTrack track, Member m) {
		this.queuelist.add(track);
		this.whoqueued.add(m);
		this.whoqueuedid.add(m.getIdLong());
		if(controller.getPlayer().getPlayingTrack() == null) {
			next();
		}
	}
	public MusicController getController() {
		return controller;
	}

	public void setController(MusicController controller) {
		this.controller = controller;
	}
	
	public List<AudioTrack> getQueuelist(){
		return queuelist;
	}
	public void setQueuelist(List<AudioTrack> queuelist) {
		this.queuelist = queuelist;
	}
	
	
	public List<Member> getWhoQueued(){
		return whoqueued;
	}
	public void setWhoQueued(List<Member> whoqueued) {
		this.whoqueued = whoqueued;
	}
	public List<Long> getWhoQueuedId(){
		return whoqueuedid;
	}
	public void setWhoQueuedId(List<Long> whoqueuedid) {
		this.whoqueuedid = whoqueuedid;
	}
}

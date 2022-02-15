package de.melody.tools.audioplayer;

import java.util.ArrayList;
import java.util.Collections;

public class Queue {
	
	private final AudioController controller;
	private final ArrayList<QueuedTrack> queuelist;
	
	public Queue(AudioController controller) {
		this.controller = controller;
		this.queuelist = new ArrayList<QueuedTrack>();
	}
	
	public void shuffleQueue() {
		Collections.shuffle(queuelist);
	} 
	
	public AudioController getController() {
		return controller;
	}
}

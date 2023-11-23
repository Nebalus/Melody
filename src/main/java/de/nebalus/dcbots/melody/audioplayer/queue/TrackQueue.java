package de.nebalus.dcbots.melody.audioplayer.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import de.nebalus.dcbots.melody.audioplayer.GuildAudioController;

public class TrackQueue {

	private final GuildAudioController controller;
	private final Queue<QueuedTrack> trackQueue;
	private final Stack<QueuedTrack> historyStack;

	public TrackQueue(GuildAudioController controller) {
		this.controller = controller;
		trackQueue = new LinkedList<>();
		historyStack = new Stack<>();
	}

	public void next() {

	}

	public void back() {

	}
}

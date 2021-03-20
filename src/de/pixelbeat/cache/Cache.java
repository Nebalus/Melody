package de.pixelbeat.cache;

import java.util.ArrayList;
import java.util.HashMap;

public class Cache {

	private HashMap<Long, ArrayList<String>> cachelist;
	private CacheController cache;
	
	public Cache(CacheController cache) {
		this.setController(cache);
	}
	
	public void setCachelist(HashMap<Long, ArrayList<String>> cachelist) {

	}
	
	public HashMap<Long, ArrayList<String>> getCachelist() {
		return cachelist;
	}
	
	public CacheController getController() {
		return cache;
	}
	
	public void setController(CacheController cache) {
		this.cache = cache;
	}
}
/*
*	
*
*
*
*
*
*
*
*
*
*
*/
package de.melody.music;

import java.util.List;

public enum Service {
	YOUTUBE(1, List.of("youtube.com","youtu.be"), "https://www.youtube.com/watch?v="),
	DISCORD(2, List.of("discordapp.com"), null),
	SPOTIFY(3, List.of("spotify.com"), null);
	//TWITCH(List.of("twitch.tv"));
	//("vimeo.com");		
	//("bandcamp.com");		
	
	List<String> validdomains;
	int databaseid;
	String shorturl;
	
	Service(int databaseid, List<String> validdomains, String shorturl){
		this.validdomains = validdomains;
		this.databaseid = databaseid;
		this.shorturl = shorturl;
	}
	
	public List<String> getValidDomains(){
		return validdomains;
	}
	
	public String getShortURL(){
		return shorturl;
	}
	
	public static Service getFromDomain(String domain) {
		for(Service service : Service.values()) {
			for(String domains : service.getValidDomains()) {
				if(domain.endsWith(domains)){
					return service;
				}	
			}
		}
		return null;
	}
	
	public static Service valueOfID(int id) {
		for(Service serv : Service.values()) {
			if(serv.databaseid == id) {
				return serv;
			}
		}
		return YOUTUBE;
	}
}

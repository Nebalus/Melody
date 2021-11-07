package de.melody.music;

import java.util.List;

public enum Service {
	YOUTUBE(List.of("youtube.com","youtu.be")),
	DISCORD(List.of("discordapp.com")),
	SPOTIFY(List.of("spotify.com"));
	//TWITCH(List.of("twitch.tv"));
	//("vimeo.com");		
	//("bandcamp.com");		
	List<String> validdomains;
	
	Service(List<String> validdomains){
		this.validdomains = validdomains;
	}
	
	public List<String> getValidDomains(){
		return validdomains;
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
}

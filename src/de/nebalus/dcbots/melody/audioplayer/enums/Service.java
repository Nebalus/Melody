package de.nebalus.dcbots.melody.audioplayer.enums;

import java.util.List;

public enum Service {
	YOUTUBE(List.of("youtube.com", "youtu.be"), "https://%domain%/watch?v=");
	
	private final List<String> validTopLevelDomains;
	private final String baseUrl;
	
	Service(List<String> validTopLevelDomains, String baseUrl) {
		this.validTopLevelDomains = validTopLevelDomains;
		this.baseUrl = baseUrl;
	}
	
	public List<String> getValidTopLevelDomains() {
		return validTopLevelDomains;
	}
	
	public String getBaseURL() {
		return baseUrl;
	}
	
	public static Service getFromDomain(String domain) {
		domain = domain.toLowerCase();
		
		for (Service service : Service.values()) {
			for (String domains : service.getValidTopLevelDomains()) {
				if (domain.endsWith(domains)) {
					return service;
				}
			}
		}
		return null;
	}
}

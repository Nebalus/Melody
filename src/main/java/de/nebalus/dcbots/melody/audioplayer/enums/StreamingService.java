package de.nebalus.dcbots.melody.audioplayer.enums;

import java.util.List;

public enum StreamingService {
	YOUTUBE(List.of("youtube.com", "youtu.be"), "https://%domain%/watch?v=");

	private final List<String> validTopLevelDomains;
	private final String baseUrl;

	StreamingService(List<String> validTopLevelDomains, String baseUrl) {
		this.validTopLevelDomains = validTopLevelDomains;
		this.baseUrl = baseUrl;
	}

	public List<String> getValidTopLevelDomains() {
		return validTopLevelDomains;
	}

	public String getBaseURL() {
		return baseUrl;
	}

	public static StreamingService getFromDomain(String domain) {
		domain = domain.toLowerCase();

		for (StreamingService streamingService : StreamingService.values()) {
			for (String domains : streamingService.getValidTopLevelDomains()) {
				if (domain.endsWith(domains)) {
					return streamingService;
				}
			}
		}
		return null;
	}
}

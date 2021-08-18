package de.melody.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.json.JSONObject;

public class SpotifyUtils {
	
	private String clientId;
	private String clientSecret;
	private JSONObject container;
	private Long expiretime = 0l;
	
	public SpotifyUtils(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		update();
	}
	
	public String getToken() {
		String token = container.getString("access_token");
		update();
		return token;
	}
	
	
	
	public void update() {
		if(expiretime < System.currentTimeMillis()) {
			final String encodedid = Base64.getEncoder().encodeToString((clientId+":"+clientSecret).getBytes());
			try {
				URL url = new URL("https://accounts.spotify.com/api/token");
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST");
				    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				    conn.setRequestProperty("Authorization", "Basic "+ encodedid);
				    conn.setRequestProperty("Accept", "application/json");
				    conn.setDoOutput(true);
				    
				try(OutputStream os = conn.getOutputStream()) {
				        os.write("grant_type=client_credentials".getBytes());
				    }
			
				try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
					StringBuilder response = new StringBuilder();
					String responseLine = null;
					while ((responseLine = br.readLine()) != null) {
				    	response.append(responseLine.trim());
				    	}
					String jsonraw = new String(response);
					container = new JSONObject(jsonraw);
					expiretime = System.currentTimeMillis() + (1000* container.getInt("expires_in"));
					
					System.out.println(jsonraw);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}

package de.pixelbeat.entities;

import de.pixelbeat.PixelBeat;
import net.dv8tion.jda.api.entities.User;

public class UserEntity {
	
	private Long userid;
	private Long expiretime;
	
	public UserEntity(User user) {
		this.userid = user.getIdLong();
		this.expiretime = System.currentTimeMillis() + PixelBeat.expiretime;
	}
	
	public Long getId() {
		return this.userid;
	}
	
	public Long getExpireTime() {
		return this.expiretime;
	}
}

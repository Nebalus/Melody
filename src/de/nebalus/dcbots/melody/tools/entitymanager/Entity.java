package de.nebalus.dcbots.melody.tools.entitymanager;

import de.nebalus.dcbots.melody.core.constants.Settings;

public class Entity {

	private Long expiretime;
	private Boolean needtoexport;
	
	protected Entity() {
		this.expiretime = System.currentTimeMillis() + Settings.ENTITY_EXPIRE_TIME;
		this.needtoexport = true;
	}
	
	public Long getExpireTime() {
		return this.expiretime;
	}
	
	public boolean needToExport() {
		return needtoexport;
	}
	
	protected void setNeedToExport(boolean value) {
		needtoexport = value;
	}
	
	protected void update() {
		this.needtoexport = true;
		renewExpireTime();
	}
	
	protected void renewExpireTime() {
		this.expiretime = System.currentTimeMillis() + Settings.ENTITY_EXPIRE_TIME;
	}
}

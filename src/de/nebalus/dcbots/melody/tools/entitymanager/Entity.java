package de.nebalus.dcbots.melody.tools.entitymanager;

import java.util.concurrent.ConcurrentHashMap;

import de.nebalus.dcbots.melody.core.constants.Settings;

public class Entity
{

	private Long expiretime;
	private Boolean needtoexport;
	
	private final ConcurrentHashMap<String, DatabaseValueContainer> dbvalues;
	
	protected Entity() 
	{
		this.expiretime = System.currentTimeMillis() + Settings.ENTITY_EXPIRE_TIME;
		this.needtoexport = false;
		
		this.dbvalues = new ConcurrentHashMap<String, DatabaseValueContainer>();
	}
	 
	protected void updateDatabaseValue(String valuename, Object value) 
	{	
		DatabaseValueContainer dvc = getDatabaseValueContainer(valuename);
		if(dvc.updateValue(value)) {
			setNeedToExport(true);
		}
	}
	
	protected void updateDatabaseValue(DatabaseValueContainer dvc, Object value) 
	{
		if(dvc.updateValue(value)) {
			setNeedToExport(true);
		}
	}
	
	protected Object getDatabaseValue(String valuename) 
	{
		renewExpireTime();
		DatabaseValueContainer dvc = getDatabaseValueContainer(valuename);
		return dvc.getValue();
	}
	
	protected Object getDatabaseValue(DatabaseValueContainer dvc) 
	{
		renewExpireTime();
		return dvc.getValue();
	}
	
	protected DatabaseValueContainer getDatabaseValueContainer(String valuename) 
	{
		if(dbvalues.containsKey(valuename)) 
		{
			DatabaseValueContainer dvc = dbvalues.get(valuename);
			return dvc;
		}
		else
		{
			throw new NullPointerException("The DatabaseValueContainer ID:" + valuename + " does not exist!");
		}
	}
	
	protected boolean createDatabaseValueContainer(DatabaseValueContainer newdvc) 
	{
		if(!dbvalues.containsKey(newdvc.getKey())) {
			dbvalues.put(newdvc.getKey(), newdvc);
			return true;
		}
		return false;
	}
	
	public boolean isExpired() 
	{
		return this.expiretime <= System.currentTimeMillis();
	}
	
	public Long getExpireTime() 
	{
		return this.expiretime;
	}
	
	public boolean needToExport() 
	{
		return this.needtoexport;
	}
	
	protected void setNeedToExport(boolean value) 
	{
		this.needtoexport = value;
	}

	protected void renewExpireTime() 
	{
		this.expiretime = System.currentTimeMillis() + Settings.ENTITY_EXPIRE_TIME;
	}
}

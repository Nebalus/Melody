package de.nebalus.dcbots.melody.tools.entitymanager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseValueContainer 
{

	public Object value;
	public final Object defaultvalue;
	public final String key;
	public final boolean canbeexported;
	public boolean needtoexport = false;
	
	public DatabaseValueContainer(String key, Boolean canbeexported, Object defaultvalue, Object value) 
	{
		this.canbeexported = canbeexported;
		this.key = key;
		this.value = value;
		this.defaultvalue = defaultvalue;
	}
	
	public DatabaseValueContainer(String key, Boolean canbeexported, Object defaultvalue) 
	{
		this.canbeexported = canbeexported;
		this.value = defaultvalue;
		this.key = key;
		this.defaultvalue = defaultvalue;
	}
	
	public void updateValue(Object value) 
	{
		needtoexport = true;
		this.value = value;
	}
	
	public Object getValue() 
	{
		if (this.value != null) 
		{
			return this.value;
		} 
		else 
		{
			updateValue(this.defaultvalue);
			return this.value;
		}
	}	
	
	public String getKey() 
	{
		return this.key;
	}
	
	public void exportValueToDatabaseRequest(PreparedStatement ps, int ioption) throws SQLException 
	{	
		if(this.value instanceof Integer) 
		{
			ps.setInt(ioption, (int) this.value);
			this.needtoexport = false;
			return;
		}
		else if(this.value instanceof String) 
		{
			ps.setString(ioption, (String) this.value);
			this.needtoexport = false;
			return;
		}
		else if(this.value instanceof Boolean) 
		{
			ps.setBoolean(ioption, (Boolean) this.value);
			this.needtoexport = false;
			return;
		}
		else if(this.value instanceof Long) 
		{
			ps.setLong(ioption, (Long) this.value);
			this.needtoexport = false;
			return;
		}
		else if(this.value instanceof Float) 
		{
			ps.setFloat(ioption, (Float) this.value);
			this.needtoexport = false;
			return;
		}
		else if(this.value instanceof Double) 
		{
			ps.setDouble(ioption, (Double) this.value);
			this.needtoexport = false;
			return;
		}
		
		throw new NullPointerException("There is no Datatype set for the value (" + this.value + ")");
	}
}

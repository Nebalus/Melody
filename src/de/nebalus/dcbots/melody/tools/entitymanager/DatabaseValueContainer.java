package de.nebalus.dcbots.melody.tools.entitymanager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseValueContainer 
{

	private Object value;
	private final Object defaultvalue;
	private final String key;
	private final boolean canbeexported;
	private boolean needtoexport = false;
	
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
	
	public boolean updateValue(Object value) 
	{
		if(!this.value.equals(value)) 
		{
			needtoexport = true;
			this.value = value;	
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean updateValue(Object value, boolean noexport) 
	{
		if(!this.value.equals(value)) 
		{
			if(!noexport) 
			{
				needtoexport = true;
			}
			this.value = value;
			return true;
		}
		else
		{
			return false;
		}
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
	
	public boolean needToExport() 
	{
		if(this.canbeexported)
		{
			return this.needtoexport;
		}
		return false;
	}
	
	public DatabaseValueContainer clone() 
	{
		return new DatabaseValueContainer(this.key, this.canbeexported, this.defaultvalue, this.value);
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

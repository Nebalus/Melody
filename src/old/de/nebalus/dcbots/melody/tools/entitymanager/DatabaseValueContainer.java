package old.de.nebalus.dcbots.melody.tools.entitymanager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseValueContainer implements Cloneable {

	private Object value;
	private final Object defaultvalue;
	private final String key;
	private final boolean canbeexported;
	private boolean needtoexport = false;

	public DatabaseValueContainer(String key, Boolean canbeexported, Object defaultvalue, Object value) {
		this.canbeexported = canbeexported;
		this.key = key;
		this.value = value;
		this.defaultvalue = defaultvalue;
	}

	public DatabaseValueContainer(String key, Boolean canbeexported, Object defaultvalue) {
		this.canbeexported = canbeexported;
		value = defaultvalue;
		this.key = key;
		this.defaultvalue = defaultvalue;
	}

	public boolean updateValue(Object value) {
		if (!this.value.equals(value)) {
			needtoexport = true;
			this.value = value;
			return true;
		} else {
			return false;
		}
	}

	public boolean updateValue(Object value, boolean noexport) {
		if (!this.value.equals(value)) {
			if (!noexport) {
				needtoexport = true;
			}
			this.value = value;
			return true;
		} else {
			return false;
		}
	}

	public Object getValue() {
		if (value != null) {
		} else {
			updateValue(defaultvalue);
		}
		return value;
	}

	public String getKey() {
		return key;
	}

	public boolean needToExport() {
		if (canbeexported) {
			return needtoexport;
		}
		return false;
	}

	public void exportValueToDatabaseRequest(PreparedStatement ps, int ioption) throws SQLException {
		if (value instanceof Integer) {
			ps.setInt(ioption, (int) value);
			needtoexport = false;
			return;
		} else if (value instanceof String) {
			ps.setString(ioption, (String) value);
			needtoexport = false;
			return;
		} else if (value instanceof Boolean) {
			ps.setBoolean(ioption, (Boolean) value);
			needtoexport = false;
			return;
		} else if (value instanceof Long) {
			ps.setLong(ioption, (Long) value);
			needtoexport = false;
			return;
		} else if (value instanceof Float) {
			ps.setFloat(ioption, (Float) value);
			needtoexport = false;
			return;
		} else if (value instanceof Double) {
			ps.setDouble(ioption, (Double) value);
			needtoexport = false;
			return;
		}

		throw new NullPointerException("There is no Datatype set for the value (" + value + ")");
	}
}

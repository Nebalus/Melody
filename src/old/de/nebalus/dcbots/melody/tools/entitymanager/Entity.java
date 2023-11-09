package old.de.nebalus.dcbots.melody.tools.entitymanager;

import java.util.concurrent.ConcurrentHashMap;

import old.de.nebalus.dcbots.melody.core.constants.Settings;

public class Entity {

	private Long expiretime;
	private Boolean needtoexport;

	private final ConcurrentHashMap<String, DatabaseValueContainer> dbvalues;

	protected Entity() {
		expiretime = System.currentTimeMillis() + Settings.ENTITY_EXPIRE_TIME;
		needtoexport = false;

		dbvalues = new ConcurrentHashMap<>();
	}

	protected void updateDatabaseValue(String valuename, Object value) {
		DatabaseValueContainer dvc = getDatabaseValueContainer(valuename);
		if (dvc.updateValue(value)) {
			setNeedToExport(true);
		}
	}

	protected void updateDatabaseValue(DatabaseValueContainer dvc, Object value) {
		if (dvc.updateValue(value)) {
			setNeedToExport(true);
		}
	}

	protected Object getDatabaseValue(String valuename) {
		renewExpireTime();
		DatabaseValueContainer dvc = getDatabaseValueContainer(valuename);
		return dvc.getValue();
	}

	protected Object getDatabaseValue(DatabaseValueContainer dvc) {
		renewExpireTime();
		return dvc.getValue();
	}

	protected DatabaseValueContainer getDatabaseValueContainer(String valuename) {
		if (dbvalues.containsKey(valuename)) {
			DatabaseValueContainer dvc = dbvalues.get(valuename);
			return dvc;
		} else {
			throw new NullPointerException("The DatabaseValueContainer ID:" + valuename + " does not exist!");
		}
	}

	protected boolean createDatabaseValueContainer(DatabaseValueContainer newdvc) {
		if (!dbvalues.containsKey(newdvc.getKey())) {
			dbvalues.put(newdvc.getKey(), newdvc);
			return true;
		}
		return false;
	}

	public boolean isExpired() {
		return expiretime <= System.currentTimeMillis();
	}

	public Long getExpireTime() {
		return expiretime;
	}

	public boolean needToExport() {
		return needtoexport;
	}

	protected void setNeedToExport(boolean value) {
		needtoexport = value;
	}

	protected void renewExpireTime() {
		expiretime = System.currentTimeMillis() + Settings.ENTITY_EXPIRE_TIME;
	}
}

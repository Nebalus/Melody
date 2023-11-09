package old.de.nebalus.dcbots.melody.tools.datamanager.files;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import old.de.nebalus.dcbots.melody.tools.ConsoleLogger;
import old.de.nebalus.dcbots.melody.tools.datamanager.DataManager;
import old.de.nebalus.dcbots.melody.tools.datamanager.FileResource;

public class LiteSQL {

	private final Connection conn;
	private final Statement stmt;

	public LiteSQL(DataManager manager) throws SQLException {

		File databasefile = FileResource.DATABASE.getFile();

		String url = "jdbc:sqlite:" + databasefile.getPath();
		conn = DriverManager.getConnection(url);

		ConsoleLogger.info("SQLDatabase", "Connection to the database established");
		stmt = conn.createStatement();

	}

	public void disconnect() {
		try {
			if (isConnected()) {
				conn.close();
				ConsoleLogger.info("SQLDatabase", "Connection to the database disconnected");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return (conn == null ? false : true);
	}

	public Connection getConnection() {
		return conn;
	}

	public void onUpdate(String sql) {
		try {
			ConsoleLogger.debug("LITESQL onUpdate", sql);
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet onQuery(String sql) {
		try {
			ConsoleLogger.debug("LITESQL onQuery", sql);
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

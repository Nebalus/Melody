package de.nebalus.dcbots.melody.tools.datamanager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.CodeSource;

import de.nebalus.dcbots.melody.core.Melody;

public final class DataHelper {

	public static String getCurrentJarPath() {
		String path = getJarPath();
		if (path.endsWith(".jar")) {
			return path.substring(0, path.lastIndexOf("/"));
		}
		return path;
	}

	public static String getJarPath() {
		final CodeSource source = Melody.INSTANCE.getClass().getProtectionDomain().getCodeSource();
		if (source != null) {
			return source.getLocation().getPath().replaceAll("%20", " ");
		}
		return null;
	}

	public static String toString(InputStream stream) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(stream);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		for (int result = bis.read(); result != -1; result = bis.read()) {
			buf.write((byte) result);
		}
		return buf.toString("UTF-8");
	}

}

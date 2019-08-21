package dbProcs;

import java.io.File;
import java.io.IOException;

public final class Constants {
	public static final String CATALINA_BASE = System.getProperty("catalina.base");
	public static final String CATALINA_CONF = getBasePath() + File.separator + File.separator + "conf";
	public static final String DBPROP = CATALINA_CONF + File.separator + "database.properties";
	public static final String SETUP_AUTH = CATALINA_CONF + File.separator + "SecurityShepherd.auth";

	private static String getBasePath() {
		String path = "";
		try {
			path = new File("./").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
}

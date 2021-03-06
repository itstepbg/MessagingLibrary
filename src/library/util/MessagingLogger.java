package library.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MessagingLogger {
	private static Logger logger = Logger.getGlobal();
	static {
		FileHandler file;
		try {
			file = new FileHandler("basicLogging.log", true);
			SimpleFormatter formatter = new SimpleFormatter();
			file.setFormatter(formatter);
			logger.addHandler(file);
		} catch (IOException ioe) {
			logger.warning("Could not create a file...");
		}
	}

	public static Logger getLogger() {
		return Logger.getGlobal();
	}
}

package com.choonster.testmod3;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Logger {
	public static final Marker MOD_MARKER = MarkerManager.getMarker(TestMod3.MODID);

	private static org.apache.logging.log4j.Logger logger;

	public static void log(Level level, String format, Object... data) {
		logger.log(level, String.format(format, data));
	}

	public static void log(Level level, Throwable throwable, String format, Object... data) {
		logger.log(level, String.format(format, data), throwable);
	}

	public static void log(Level level, Marker marker, String format, Object... data) {
		logger.log(level, marker, String.format(format, data));
	}

	public static void log(Level level, Marker marker, Throwable throwable, String format, Object... data) {
		logger.log(level, marker, String.format(format, data), throwable);
	}

	public static void fatal(String format, Object... data) {
		log(Level.FATAL, format, data);
	}

	public static void fatal(Marker marker, String format, Object... data) {
		log(Level.FATAL, format, data);
	}

	public static void fatal(Throwable throwable, String format,
							 Object... data) {
		log(Level.FATAL, throwable, format, data);
	}

	public static void fatal(Marker marker, Throwable throwable, String format,
							 Object... data) {
		log(Level.FATAL, marker, throwable, format, data);
	}

	public static void error(String format, Object... data) {
		log(Level.ERROR, format, data);
	}

	public static void error(Marker marker, String format, Object... data) {
		log(Level.ERROR, marker, format, data);
	}

	public static void error(Throwable throwable, String format,
							 Object... data) {
		log(Level.ERROR, throwable, format, data);
	}

	public static void error(Marker marker, Throwable throwable, String format,
							 Object... data) {
		log(Level.ERROR, marker, throwable, format, data);
	}

	public static void warn(String format, Object... data) {
		log(Level.WARN, format, data);
	}

	public static void warn(Marker marker, String format, Object... data) {
		log(Level.WARN, marker, format, data);
	}

	public static void info(String format, Object... data) {
		log(Level.INFO, format, data);
	}

	public static void info(Marker marker, String format, Object... data) {
		log(Level.INFO, marker, format, data);
	}

	public static void info(Throwable throwable, String format,
							Object... data) {
		log(Level.INFO, throwable, format, data);
	}

	public static void info(Marker marker, Throwable throwable, String format,
							Object... data) {
		log(Level.INFO, marker, throwable, format, data);
	}

	public static void debug(String format, Object... data) {
		log(Level.DEBUG, format, data);
	}

	public static void debug(Marker marker, String format, Object... data) {
		log(Level.DEBUG, marker, format, data);
	}

	public static void setLogger(org.apache.logging.log4j.Logger logger) {
		if (Logger.logger != null) {
			throw new IllegalStateException("Attempt to replace logger");
		}

		Logger.logger = logger;
	}


}

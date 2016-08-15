package org.beanone.xlogger;

/**
 * Abstraction of configuration handling logic.
 *
 * @author Hongyan Li
 *
 * @param <T>
 *            the type of object that is used to categorize and lookup the
 *            configured entities.
 * @param <E>
 *            the type of entities that this handler produces from the
 *            configuration.
 */
public interface ConfigHandler<T, E> extends LoggingConfigurable {
	String METHOD_HANDLER = "loggingMethodConfigHandler";
	String EXCEPTION_HANDLER = "loggingExceptionConfigHandler";
	String FILTER_HANDLER = "loggingFilterConfigHandler";

	/**
	 * Retrieves the configured entity from the passed in object.
	 *
	 * @param type
	 *            the object that this uses to lookup for the matching
	 *            configured entity.
	 * @return a configured entity.
	 */
	E getConfiguration(T type);

	/**
	 * Post processing logic after configuration is loaded.
	 */
	void postConfigure();

	/**
	 * Converts the passed in to {@link MethodAccessLevel}.
	 *
	 * @param access
	 *            a method access level string.
	 * @return a matching method access level, case ignored.
	 */
	default MethodAccessLevel toAccessLevel(String access) {
		return MethodAccessLevel.valueOf(access.trim().toUpperCase());
	}

	/**
	 * Converts the passed in to {@link LoggerLevel}.
	 *
	 * @param levelString
	 *            a logger level string.
	 * @return a matching logger level, case ignored.
	 */
	default LoggerLevel toLoggerLevel(String levelString) {
		return LoggerLevel.valueOf(levelString.trim().toUpperCase());
	}
}

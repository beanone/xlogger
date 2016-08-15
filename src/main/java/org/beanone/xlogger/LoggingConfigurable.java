package org.beanone.xlogger;

/**
 * Abstraction of the basic logic of logging configuration.
 *
 * @author Hongyan Li
 *
 */
public interface LoggingConfigurable {
	/**
	 * Checks whether the passed in should be handled by this.
	 *
	 * @param key
	 *            configuration key to check.
	 * @return true is the passed in should be handled by this.
	 */
	default boolean accept(String key) {
		return key.startsWith(getPrefix());
	}

	/**
	 * Add a configuration entry.
	 *
	 * @param key
	 *            a configuration key.
	 * @param value
	 *            a configuration value.
	 */
	void addConfigEntry(String key, String value);

	/**
	 * Fetches the prefix for the configuration entries this support.
	 *
	 * @return a prefix string.
	 */
	String getPrefix();

	/**
	 * Strips the prefix from the passed in key.
	 *
	 * @param key
	 *            the key string whose prefix is to be stripped.
	 * @return the key with prefix stripped.
	 */
	default String stripPrefix(String key) {
		return key.substring(getPrefix().length()).trim();
	}

}
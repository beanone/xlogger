package org.beanone.xlogger.configure.impl;

import java.lang.reflect.Method;

import org.beanone.xlogger.LoggerLevel;
import org.beanone.xlogger.configure.ConfigHandler;

/**
 * {@link Method} based {@link LoggerLevel} configuration for join points.
 *
 * @author Hongyan Li
 *
 */
abstract class AbstractConfigHandler<T>
        implements ConfigHandler<T, LoggerLevel> {
	@Override
	public final void addConfigEntry(String withPrefix, String value) {
		final String key = stripPrefix(withPrefix);
		if (isDefaultKey(key)) {
			configDefault(key, value);
		} else if (key.endsWith(".*")) {
			configDefault(key.substring(0, key.length() - 2), value);
		} else {
			configure(key, value);
		}
	}

	protected boolean isDefaultKey(final String key) {
		return "*".equals(key) || key.indexOf('.') < 0;
	}

	@Override
	public void postConfigure() {
		// do nothing by default
	}

	/**
	 * Configures the configuration entry as default entry.
	 *
	 * @param key
	 *            the key with prefix stripped.
	 * @param value
	 *            the configuration string value.
	 */
	protected abstract void configDefault(String key, String value);

	/**
	 * Configures the configuration entry as a non-default entry.
	 *
	 * @param key
	 *            the key with prefix stripped.
	 * @param value
	 *            the configuration string value.
	 */
	protected abstract void configure(String key, String value);
}

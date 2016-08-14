package org.beanone.xlogger.configure.impl;

import java.util.HashMap;
import java.util.Map;

import org.beanone.xlogger.LoggerLevel;
import org.beanone.xlogger.configure.ConfigHandler;
import org.springframework.stereotype.Component;

/**
 * Handles exception logging configuration.
 *
 * @author Hongyan Li
 *
 */
@Component(ConfigHandler.EXCEPTION_HANDLER)
public class ExceptionConfigHandler extends AbstractConfigHandler<Throwable> {
	private final Map<String, LoggerLevel> configEntries = new HashMap<>();

	@Override
	public LoggerLevel getConfiguration(Throwable exception) {
		final LoggerLevel level = this.configEntries
		        .get(exception.getClass().getName());
		return level == null ? LoggerLevel.ERROR : level;
	}

	@Override
	public String getPrefix() {
		return "xlogger.exception.";
	}

	@Override
	protected void configDefault(String key, String value) {
		// do nothing;
	}

	@Override
	protected void configure(String key, String value) {
		this.configEntries.put(key, toLoggerLevel(value));
	}

}

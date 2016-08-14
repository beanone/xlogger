package org.beanone.xlogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.aspectj.lang.JoinPoint;
import org.beanone.xlogger.configure.ConfigHandler;
import org.beanone.xlogger.configure.NamespaceSupporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class XLoggerManager {
	@Autowired
	private List<ConfigHandler<?, ?>> configHandlers;

	@Autowired
	@Qualifier(ConfigHandler.METHOD_HANDLER)
	private ConfigHandler<AspectContext, LoggerLevel> methodConfigHandler;

	@Autowired
	@Qualifier(ConfigHandler.EXCEPTION_HANDLER)
	private ConfigHandler<AspectContext, LoggerLevel> exceptionConfigHandler;

	/**
	 * Retrieves the {@link LoggerLevel} for the passed in {@link Throwable}.
	 *
	 * @param context
	 *            an {@link AspectContext}.
	 * @return a {@link LoggerLevel}.
	 */
	@Cacheable(value = XLoggerConfiguration.EXCEPTION_LOGGING_CACHE_ID, keyGenerator = ExceptionLoggingCacheKeyGenerator.NAME)
	public LoggerLevel getExceptionLoggerLevel(AspectContext context) {
		return getExceptionConfigHandler().getConfiguration(context);
	}

	/**
	 * Retrieves the {@link LoggerLevel} for the passed in {@link JoinPoint}.
	 *
	 * @param context
	 *            an {@link AspectContext}.
	 * @return a {@link LoggerLevel}.
	 */
	@Cacheable(value = XLoggerConfiguration.METHOD_LOGGING_CACHE_ID, keyGenerator = MethodLoggingCacheKeyGenerator.NAME)
	public LoggerLevel getMethodLoggerLevel(AspectContext context) {
		return getMethodConfigHandler().getConfiguration(context);
	}

	/**
	 * Retrieves the {@link NamespaceSupporter} so that namspace filters can be
	 * registered.
	 *
	 * @return a {@link NamespaceSupporter}.
	 */
	public NamespaceSupporter getNamespaceSupporter() {
		return (NamespaceSupporter) getMethodConfigHandler();
	}

	/**
	 * Initializes this with an {@link InputStream} that has the xlogger
	 * configuration.
	 *
	 * @param inStream
	 *            an {@link InputStream} that for the xlogger configuration
	 *            properties file.
	 * @throws IOException
	 *             if error while reading the configuration.
	 */
	public void init(InputStream inStream) throws IOException {
		final Properties config = new Properties();
		config.load(inStream);
		configure(config);
	}

	private void configure(Properties configuration) {
		configuration.entrySet().forEach(entry -> {
			final String key = entry.getKey().toString();
			final Optional<ConfigHandler<?, ?>> handler = this.configHandlers
		            .stream().filter(h -> h.accept(key)).findFirst();
			if (handler.isPresent()) {
				handler.get().addConfigEntry(key, entry.getValue().toString());
			}
		});
	}

	private ConfigHandler<AspectContext, LoggerLevel> getExceptionConfigHandler() {
		return this.exceptionConfigHandler;
	}

	private ConfigHandler<AspectContext, LoggerLevel> getMethodConfigHandler() {
		return this.methodConfigHandler;
	}
}

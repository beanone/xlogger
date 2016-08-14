package org.beanone.xlogger.configure.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.beanone.xlogger.AspectContext;
import org.beanone.xlogger.LoggerLevel;
import org.beanone.xlogger.MethodAccessLevel;
import org.springframework.stereotype.Component;

/**
 * Handles {@link Package} based {@link LoggerLevel} configuration for join
 * points.
 *
 * @author Hongyan Li
 *
 */
@Component
public class PackageConfigHandler extends AbstractConfigHandler<AspectContext> {
	private class PackageWalker {
		private final StringBuilder builder;

		PackageWalker(Package pkg) {
			this.builder = new StringBuilder(pkg.getName());
		}

		String current() {
			return this.builder.toString();
		}

		boolean up() {
			final int index = this.builder.lastIndexOf(".");
			if (index < 0) {
				return false;
			}
			this.builder.delete(index, this.builder.length());
			return true;
		}
	}

	private final Map<String, LoggerLevel> configEntries = new HashMap<>();

	private final Map<String, LoggerLevel> defaultLevels = new HashMap<>();

	@Override
	public LoggerLevel getConfiguration(AspectContext context) {
		final Method method = context.getMethod();
		final Package pkg = method.getDeclaringClass().getPackage();
		final PackageWalker pw = new PackageWalker(pkg);
		LoggerLevel level;
		while ((level = resolveLoggerLevel(pw.current(),
		        context.getAccess())) == null) {
			if (!pw.up()) {
				break;
			}
		}
		return level == null ? this.defaultLevels.get(pkg.getName()) : level;
	}

	@Override
	public String getPrefix() {
		return "xlogger.package.";
	}

	public void reset() {
		this.configEntries.clear();
		this.defaultLevels.clear();
	}

	@Override
	protected void configDefault(String key, String value) {
		this.defaultLevels.put(key, toLoggerLevel(value));
	}

	@Override
	protected void configure(String key, String value) {
		this.configEntries.put(key, toLoggerLevel(value));
	}

	protected LoggerLevel resolveLoggerLevel(final String packageName,
	        final MethodAccessLevel access) {
		final String key = packageName + '.' + access.name().toLowerCase();
		return this.configEntries.get(key);
	}
}

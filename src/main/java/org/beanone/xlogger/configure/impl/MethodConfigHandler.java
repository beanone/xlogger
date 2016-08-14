package org.beanone.xlogger.configure.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import org.aspectj.lang.JoinPoint;
import org.beanone.xlogger.AspectContext;
import org.beanone.xlogger.LoggerLevel;
import org.beanone.xlogger.MethodAccessLevel;
import org.beanone.xlogger.configure.ConfigHandler;
import org.beanone.xlogger.configure.NamespaceSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles {@link Method} based {@link LoggerLevel} configuration for join
 * points.
 *
 * @author Hongyan Li
 *
 */
@Component(ConfigHandler.METHOD_HANDLER)
public class MethodConfigHandler extends AbstractConfigHandler<AspectContext>
        implements NamespaceSupporter {
	private static final Logger LOGGER = LoggerFactory
	        .getLogger(MethodConfigHandler.class);

	private final Map<MethodAccessLevel, LoggerLevel> configEntities = new HashMap<>();
	private final AtomicReference<LoggerLevel> defaultLevel = new AtomicReference<>(
	        LoggerLevel.TRACE);

	@Autowired
	private NamespacedConfigHandler namespaceHandler;

	@Autowired
	private PackageConfigHandler packageHandler;

	@Override
	public LoggerLevel getConfiguration(AspectContext context) {
		LoggerLevel level = this.namespaceHandler.getConfiguration(context);
		if (level == null) {
			level = this.configEntities.get(context.getAccess());
		}
		return level == null ? getLoggerByPackage(context) : level;
	}

	public NamespacedConfigHandler getNamespaceHandler() {
		return this.namespaceHandler;
	}

	public PackageConfigHandler getPackageHandler() {
		return this.packageHandler;
	}

	@Override
	public String getPrefix() {
		return "xlogger.method.";
	}

	@Override
	public void registerClassCondition(String ns,
	        Predicate<Class<?>> condition) {
		final NamespaceRegister register = this.namespaceHandler
		        .getRegister(ns);
		if (register == null) {
			LOGGER.warn(
			        "Trying to register a class based condition with undefined logging namespace {}.",
			        ns);
		} else {
			register.registerClassCondition(condition);
		}
	}

	@Override
	public void registerJoinPointCondition(String ns,
	        Predicate<JoinPoint> condition) {
		final NamespaceRegister register = this.namespaceHandler
		        .getRegister(ns);
		if (register == null) {
			LOGGER.warn(
			        "Trying to register a join point based condition with undefined logging namespace {}.",
			        ns);
		} else {
			register.registerJoinPointCondition(condition);
		}
	}

	@Override
	public void registerMethodCondition(String ns,
	        Predicate<Method> condition) {
		final NamespaceRegister register = this.namespaceHandler
		        .getRegister(ns);
		if (register == null) {
			LOGGER.warn(
			        "Trying to register a method based condition with undefined logging namespace {}.",
			        ns);
		} else {
			register.registerMethodCondition(condition);
		}
	}

	@Override
	public void registerPackageCondition(String ns,
	        Predicate<Package> condition) {
		final NamespaceRegister register = this.namespaceHandler
		        .getRegister(ns);
		if (register == null) {
			LOGGER.warn(
			        "Trying to register a package based condition with undefined logging namespace {}.",
			        ns);
		} else {
			register.registerPackageCondition(condition);
		}
	}

	public void reset() {
		this.configEntities.clear();
		this.defaultLevel.set(LoggerLevel.TRACE);
	}

	private LoggerLevel getLoggerByPackage(AspectContext context) {
		final LoggerLevel level = this.packageHandler.getConfiguration(context);
		return level == null ? this.defaultLevel.get() : level;
	}

	@Override
	protected void configDefault(String key, String value) {
		this.defaultLevel.set(toLoggerLevel(value));
	}

	@Override
	protected void configure(String key, String value) {
		this.configEntities.put(toAccessLevel(key), toLoggerLevel(value));
	}

	@Override
	protected boolean isDefaultKey(final String key) {
		return "*".equals(key);
	}
}

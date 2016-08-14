package org.beanone.xlogger.configure.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.beanone.xlogger.LoggerLevel;
import org.beanone.xlogger.MethodAccessLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles namespace based {@link LoggerLevel} configuration for join points.
 *
 * @author Hongyan Li
 *
 */
@Component
public class NamespacedConfigHandler extends AbstractConfigHandler<JoinPoint> {
	private static final Logger LOGGER = LoggerFactory
	        .getLogger(NamespacedConfigHandler.class);

	private final Map<String, LoggerLevel> configEntries = new HashMap<>();
	private final Map<String, LoggerLevel> defaultLevels = new HashMap<>();
	private final Map<String, NamespaceRegister> nsMap = new HashMap<>();

	@Autowired
	private NamespaceFilterHandler filterHandler;

	@Override
	public LoggerLevel getConfiguration(JoinPoint point) {
		final Optional<NamespaceRegister> register = this.nsMap.values()
		        .stream().filter(p -> p.test(point)).findFirst();
		if (register.isPresent()) {
			final String ns = register.get().getNamespace();
			final Method method = ConfigUtils.getMethod(point);
			final MethodAccessLevel access = ConfigUtils.getAccessLevel(method);
			final LoggerLevel level = this.configEntries
			        .get(ns + '.' + access.name().toLowerCase());
			return level == null ? this.defaultLevels.get(ns) : level;
		}
		return null;
	}

	@Override
	public String getPrefix() {
		return "xlogger.ns.";
	}

	public NamespaceRegister getRegister(String ns) {
		return this.nsMap.get(ns);
	}

	@Override
	public void postConfigure() {
		this.filterHandler.processConfiguration((ns, configurations) -> {
			configurations.forEach(config -> {
			    final boolean processed = processClassCondition(ns, config)
		                || processPackageCondition(ns, config)
		                || processMethodCondition(ns, config);
			    if (!processed) {
				    LOGGER.warn(
		                    "Invalid filter configuration {} for namespace {}!",
		                    config, ns);
			    }
		    });
		});
	}

	public void reset() {
		this.configEntries.clear();
		this.defaultLevels.clear();
		this.nsMap.clear();
	}

	public void setFilterHandler(NamespaceFilterHandler filterHandler) {
		this.filterHandler = filterHandler;
	}

	private String getNamespace(String key) {
		final int index = key.lastIndexOf('.');

		// essentially validating the access modifier been valid
		toAccessLevel(key.substring(index + 1));

		return key.substring(0, index);
	}

	private void initNamespaceIfNotYet(String ns) {
		if (this.nsMap.get(ns) == null) {
			this.nsMap.put(ns, new NamespaceRegister(ns));
		}
	}

	private boolean processClassCondition(String ns, String config) {
		final String stripped = startWithStrip(config, "class,");
		if (stripped != null) {
			getRegister(ns)
			        .registerClassCondition(p -> p.getName().equals(stripped));
			return true;
		}
		return false;
	}

	private boolean processMethodCondition(String ns, String config) {
		final String stripped = startWithStrip(config, "method,");
		if (stripped != null) {
			getRegister(ns).registerMethodCondition(
			        m -> (m.getDeclaringClass().getName() + '.' + m.getName())
			                .equals(stripped));
			return true;
		}
		return false;
	}

	private boolean processPackageCondition(String ns, String config) {
		final String stripped = startWithStrip(config, "package,");
		if (stripped != null) {
			getRegister(ns).registerPackageCondition(
			        p -> p.getName().equals(stripped));
			return true;
		}
		return false;
	}

	private String startWithStrip(String str, String prefix) {
		return str.startsWith(prefix) ? str.substring(prefix.length()).trim()
		        : null;
	}

	@Override
	protected void configDefault(String key, String value) {
		this.defaultLevels.put(key, toLoggerLevel(value));
		initNamespaceIfNotYet(key);
	}

	@Override
	protected void configure(String key, String value) {
		this.configEntries.put(key, toLoggerLevel(value));
		final String ns = getNamespace(key);
		initNamespaceIfNotYet(ns);
	}
}

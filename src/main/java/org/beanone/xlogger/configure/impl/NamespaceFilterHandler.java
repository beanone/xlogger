package org.beanone.xlogger.configure.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.beanone.xlogger.configure.ConfigHandler;
import org.springframework.stereotype.Component;

/**
 * Handles logging namespace filter configurations.
 *
 * @author Hongyan Li
 *
 */
@Component(ConfigHandler.FILTER_HANDLER)
public class NamespaceFilterHandler
        implements ConfigHandler<String, List<String>> {
	// map from namespace to list of filter configurations
	private final Map<String, List<String>> configEntries = new HashMap<>();

	@Override
	public void addConfigEntry(String withPrefix, String value) {
		final String key = stripPrefix(withPrefix);
		final String ns = getNamespace(key);
		List<String> filterConfigs = this.configEntries.get(ns);
		if (filterConfigs == null) {
			filterConfigs = new ArrayList<>();
			this.configEntries.put(ns, filterConfigs);
		}
		filterConfigs.add(value);
	}

	@Override
	public List<String> getConfiguration(String ns) {
		return this.configEntries.get(ns);
	}

	@Override
	public String getPrefix() {
		return "filter.";
	}

	@Override
	public void postConfigure() {
		// do nothing by default
	}

	private String getNamespace(String key) {
		final int index = key.lastIndexOf('.');
		if (index > 0) {
			return key.substring(0, index);
		}
		return key;
	}

	void processConfiguration(BiConsumer<String, List<String>> consumer) {
		this.configEntries.forEach(consumer);
	}
}

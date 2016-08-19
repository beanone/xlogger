package org.beanone.xlogger.configure;

import org.apache.commons.lang.StringUtils;
import org.beanone.xlogger.ArgumentSpecRegistry;
import org.beanone.xlogger.ConfigHandler;
import org.beanone.xlogger.ConfiguredArgumentSpec;
import org.springframework.stereotype.Component;

@Component
public class ArgumentSpecConfigHandler
        implements ConfigHandler<String, String> {
	@SuppressWarnings("unchecked")
	@Override
	public void addConfigEntry(String withPrefix, String value) {
		final String key = stripPrefix(withPrefix);
		String profile = "";
		final int index = key.indexOf('.');
		if (index > 0) {
			profile = key.substring(0, index);
		}
		final String className = key.substring(index + 1);

		if (StringUtils.isBlank(className)) {
			throw new IllegalArgumentException(
			        "Invalid xlogger configuration. Class name cannot be blank in spec configuration!");
		}

		try {
			if (ArgumentSpecRegistry.current(profile) == null) {
				ArgumentSpecRegistry.initProfile(profile);
			}
			ArgumentSpecRegistry.current(profile).register(
			        Class.forName(className),
			        new ConfiguredArgumentSpec(profile, value));
		} catch (final ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public String getConfiguration(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getPrefix() {
		return "spec.";
	}

	@Override
	public void postConfigure() {
		// do nothing
	}

}

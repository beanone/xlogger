package org.beanone.xlogger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represent an {@link ArgumentSpec} that can be configured from configuration.
 *
 * @author Hongyan Li
 *
 */
@SuppressWarnings("rawtypes")
public class ConfiguredArgumentSpec implements ArgumentSpec {
	private static final Logger LOGGER = LoggerFactory
	        .getLogger(ConfiguredArgumentSpec.class);
	private final String partition;
	private final List<String> attributes = new ArrayList<>();

	/**
	 * Create an instance of this.
	 *
	 * @param partition
	 *            the partition the ArgumentSpec lives in.
	 * @param configuration
	 *            the configuration.
	 */
	public ConfiguredArgumentSpec(String partition, String configuration) {
		this.partition = partition;
		final StringTokenizer st = new StringTokenizer(configuration, ",");
		while (st.hasMoreTokens()) {
			this.attributes.add(st.nextToken().trim());
		}
	}

	@Override
	public String describe(Object arg) {
		final StringBuilder sb = new StringBuilder();
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry
		        .current(this.partition);
		this.attributes.forEach(a -> {
			try {
				final Object value = PropertyUtils.getProperty(arg, a);
				sb.append(a).append('=');
				if (value != null) {
					final ArgumentSpec spec = registry
		                    .getSpec(value.getClass());
					sb.append(spec.asString(value));
				}
				sb.append(", ");
			} catch (final IllegalAccessException | InvocationTargetException
		            | NoSuchMethodException e) {
				LOGGER.warn("Failed to get property {} from object {}", a, arg);
			}
		});
		if (sb.length() > 0) {
			sb.delete(sb.length() - 2, sb.length());
			sb.insert(0, '{').append('}');
		}
		return sb.toString();
	}

}

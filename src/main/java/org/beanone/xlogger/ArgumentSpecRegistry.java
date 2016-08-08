package org.beanone.xlogger;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Registry for {@link ArgumentSpec}s.
 *
 * @author Hongyan Li
 *
 */
public class ArgumentSpecRegistry {
	private static final ArgumentSpecRegistry BASE = new ArgumentSpecRegistry() {
		@Override
		public <T> ArgumentSpec<T> getSpec(Class<T> clazz) {
			final ArgumentSpec<T> returns = super.getSpec(clazz);
			return (returns == null) ? register(clazz, arg -> arg.toString())
	                : returns;
		}
	};

	private static final Map<String, ArgumentSpecRegistry> partitions = new HashMap<>();

	private final Map<Class<?>, ArgumentSpec<?>> argumentSpecs = new HashMap<>();

	private ArgumentSpecRegistry() {
		// private for singleton
	}

	/**
	 * @return the default {@link ArgumentSpec}.
	 */
	public static ArgumentSpecRegistry current() {
		return BASE;
	}

	/**
	 * @param partitionName
	 *            the name of a partition of {@link ArgumentSpecRegistry}. Value
	 *            trimmed before use.
	 * @return a {@link ArgumentSpecRegistry} for the passed in partition.
	 */
	public static ArgumentSpecRegistry current(String partitionName) {
		return StringUtils.isBlank(partitionName) ? BASE
		        : partitions.get(partitionName.trim());
	}

	/**
	 * Initializes the passed in partition by name.
	 *
	 * @param partitionName
	 *            the name of the partition to be initialized. Value trimmed
	 *            before use.
	 */
	public static void initPartition(String partitionName) {
		partitions.put(partitionName.trim(), new ArgumentSpecRegistry() {
			@Override
			public <T> ArgumentSpec<T> getSpec(Class<T> clazz) {
				final ArgumentSpec<T> returns = super.getSpec(clazz);
				return (returns == null) ? BASE.getSpec(clazz) : returns;
			}
		});
	}

	/**
	 * Fetches the {@link ArgumentSpec} for the passed in class type.
	 *
	 * @param clazz
	 *            the class type for the {@link ArgumentSpec} to be retrieved.
	 * @param <T>
	 *            the class type retrieved {@link ArgumentSpec} is for.
	 * @return a matching {@link ArgumentSpec}.
	 */
	@SuppressWarnings("unchecked")
	public <T> ArgumentSpec<T> getSpec(Class<T> clazz) {
		return (ArgumentSpec<T>) this.argumentSpecs.get(clazz);
	}

	/**
	 * Registers the passed in ArgumentSpec with the passed in class type.
	 *
	 * @param clazz
	 *            the class type the passed in {@link ArgumentSpec} is for.
	 * @param spec
	 *            the {@link ArgumentSpec} to register.
	 * @param <T>
	 *            the class type registering {@link ArgumentSpec} is for.
	 * @return the spec passed in.
	 */
	public <T> ArgumentSpec<T> register(Class<T> clazz, ArgumentSpec<T> spec) {
		this.argumentSpecs.put(clazz, spec);
		return spec;
	}
}

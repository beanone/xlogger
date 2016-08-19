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

	private static final Map<String, ArgumentSpecRegistry> profiles = new HashMap<>();

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
	 * @param profileName
	 *            the name of a profile of {@link ArgumentSpecRegistry}. Value
	 *            trimmed before use.
	 * @return a {@link ArgumentSpecRegistry} for the passed in profile.
	 */
	public static ArgumentSpecRegistry current(String profileName) {
		LogExecutionContext.current().setProfile(profileName);
		return StringUtils.isBlank(profileName) ? BASE
		        : profiles.get(profileName.trim());
	}

	/**
	 * Initializes the passed in profile by name.
	 *
	 * @param profileName
	 *            the name of the profile to be initialized. Value trimmed
	 *            before use.
	 */
	public static void initProfile(String profileName) {
		profiles.put(profileName.trim(), new ArgumentSpecRegistry() {
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
		if (Iterable.class.isAssignableFrom(clazz)) {
			return (ArgumentSpec<T>) this.argumentSpecs.get(Iterable.class);
		} else if (Map.class.isAssignableFrom(clazz)) {
			return (ArgumentSpec<T>) this.argumentSpecs.get(Map.class);
		}
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

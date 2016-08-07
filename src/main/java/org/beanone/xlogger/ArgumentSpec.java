package org.beanone.xlogger;

/**
 *
 * @author Hongyan Li
 *
 * @param <T>
 *            Argument type the spec if for.
 */
@FunctionalInterface
public interface ArgumentSpec<T> {
	/**
	 * Describes the passed in argument Object.
	 *
	 * @param arg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default String asString(Object arg) {
		return describe((T) arg);
	}

	/**
	 * Describes the passed in argument.
	 *
	 * @param arg
	 * @return
	 */
	String describe(T arg);
}

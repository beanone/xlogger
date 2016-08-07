package org.beanone.xlogger;

/**
 * Encapsulates the logic of turning something into a String.
 *
 * @author Hongyan Li
 *
 */
@FunctionalInterface
public interface Stringnifier {
	/**
	 * Renders something into String.
	 *
	 * @return a String.
	 */
	String asString();
}

package org.beanone.xlogger;

import org.slf4j.Logger;

/**
 * Encapsulates the handling of logging on behalf of a logger.
 *
 * @author Hongyan Li
 *
 */
@FunctionalInterface
public interface LoggingHandler {
	/**
	 * Handles logging on behalf of the passed in logger.
	 *
	 * @param logger
	 *            a {@link Logger}.
	 * @param snf
	 *            a {@link Stringnifier} that encapsulates the logic to turn
	 *            something to String.
	 * @param t
	 *            the exception to include in logging.
	 */
	void handle(Logger logger, Stringnifier snf, Throwable t);
}

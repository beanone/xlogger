package org.beanone.xlogger;

/**
 * Defines all 5 types of {@link LoggingHandler}s.
 *
 * @author Hongyan Li
 *
 */
public class LoggingHandlers {
	public static final LoggingHandler ERROR_HANDLER = (logger, snf, t) -> {
		if (logger.isErrorEnabled()) {
			logger.error(snf.asString(), t);
		}
	};
	public static final LoggingHandler WARN_HANDLER = (logger, snf, t) -> {
		if (logger.isWarnEnabled()) {
			logger.warn(snf.asString(), t);
		}
	};
	public static final LoggingHandler DEBUG_HANDLER = (logger, snf, t) -> {
		if (logger.isDebugEnabled()) {
			logger.debug(snf.asString(), t);
		}
	};
	public static final LoggingHandler INFO_HANDLER = (logger, snf, t) -> {
		if (logger.isInfoEnabled()) {
			logger.info(snf.asString(), t);
		}
	};
	public static final LoggingHandler TRACE_HANDLER = (logger, snf, t) -> {
		if (logger.isTraceEnabled()) {
			logger.trace(snf.asString(), t);
		}
	};

	private LoggingHandlers() {
		// private for utility
	}
}

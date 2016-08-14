package org.beanone.xlogger;

import org.slf4j.Logger;

/**
 * An logging level enum that encapsulates the logging behavior.
 *
 * @author Hongyan Li
 *
 */
public enum LoggerLevel implements LoggingHandler {
	ERROR(LoggingHandlers.ERROR_HANDLER),
	WARN(LoggingHandlers.WARN_HANDLER),
	DEBUG(LoggingHandlers.DEBUG_HANDLER),
	INFO(LoggingHandlers.INFO_HANDLER),
	TRACE(LoggingHandlers.TRACE_HANDLER),
	SKIP(LoggingHandlers.SKIP_HANDLER);

	private LoggingHandler handler;

	private LoggerLevel(LoggingHandler handler) {
		this.handler = handler;
	}

	@Override
	public void handle(Logger logger, Stringnifier snf, Throwable t) {
		this.handler.handle(logger, snf, t);
	}
}

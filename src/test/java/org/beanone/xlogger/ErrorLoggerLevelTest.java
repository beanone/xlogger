package org.beanone.xlogger;

public class ErrorLoggerLevelTest extends ErrorLoggingHandlerTest {
	@Override
	protected LoggingHandler getLoggingHandler() {
		return LoggerLevel.ERROR;
	}
}

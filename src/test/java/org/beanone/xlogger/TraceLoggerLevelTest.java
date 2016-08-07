package org.beanone.xlogger;

public class TraceLoggerLevelTest extends TraceLoggingHandlerTest {
	@Override
	protected LoggingHandler getLoggingHandler() {
		return LoggerLevel.TRACE;
	}
}

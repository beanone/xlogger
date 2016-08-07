package org.beanone.xlogger;

public class DebugLoggerLevelTest extends DebugLoggingHandlerTest {
	@Override
	protected LoggingHandler getLoggingHandler() {
		return LoggerLevel.DEBUG;
	}
}

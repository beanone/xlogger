package org.beanone.xlogger;

public class WarnLoggerLevelTest extends WarnLoggingHandlerTest {
	@Override
	protected LoggingHandler getLoggingHandler() {
		return LoggerLevel.WARN;
	}
}

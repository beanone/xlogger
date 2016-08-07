package org.beanone.xlogger;

public class InfoLoggerLevelTest extends InfoLoggingHandlerTest {
	@Override
	protected LoggingHandler getLoggingHandler() {
		return LoggerLevel.INFO;
	}
}

package org.beanone.xlogger;

public class SkipLoggerLevelTest extends SkipLoggingHandlerTest {
	@Override
	protected LoggingHandler getLoggingHandler() {
		return LoggerLevel.SKIP;
	}
}

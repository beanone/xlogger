package org.beanone.xlogger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;

public class SkipLoggingHandlerTest extends LoggingHandlerTestBase {

	@Override
	@Test
	public void testHandleLevelEnabled() {
		enable(true);
		final String testMessage = "hello";
		Assert.assertNull(assertCalled(getLoggingHandler(), testMessage, 0));
	}

	@Override
	protected void enable(boolean enable) {
		Mockito.when(this.logger.isTraceEnabled()).thenReturn(enable);
	}

	@Override
	protected LoggingHandler getLoggingHandler() {
		return LoggingHandlers.SKIP_HANDLER;
	}

	@Override
	protected void verify(Logger verify, ArgumentCaptor<String> captor,
	        ArgumentCaptor<Throwable> captor2) {
		verify.trace(captor.capture(), captor2.capture());
	}
}

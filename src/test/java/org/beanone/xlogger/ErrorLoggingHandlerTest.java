package org.beanone.xlogger;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;

public class ErrorLoggingHandlerTest extends LoggingHandlerTestBase {

	@Override
	protected void enable(boolean enable) {
		Mockito.when(this.logger.isErrorEnabled()).thenReturn(enable);
	}

	@Override
	protected LoggingHandler getLoggingHandler() {
		return LoggingHandlers.ERROR_HANDLER;
	}

	@Override
	protected void verify(Logger verify, ArgumentCaptor<String> captor,
	        ArgumentCaptor<Throwable> captor2) {
		verify.error(captor.capture(), captor2.capture());
	}
}

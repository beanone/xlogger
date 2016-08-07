package org.beanone.xlogger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;

public abstract class LoggingHandlerTestBase {
	protected Logger logger;

	@Before
	public void setup() {
		this.logger = Mockito.mock(Logger.class);
	}

	@Test
	public void testHandleLevelEnabled() {
		enable(true);
		final String testMessage = "hello";
		Assert.assertEquals(testMessage,
		        assertCalled(getLoggingHandler(), testMessage, 1));
	}

	@Test
	public void testHandleLevelNotEnabled() {
		enable(false);
		final String testMessage = "hello";
		assertCalled(getLoggingHandler(), testMessage, 0);
	}

	private String assertCalled(final LoggingHandler handler,
	        final String testMessage, final int count) {
		final ArgumentCaptor<String> captor = ArgumentCaptor
		        .forClass(String.class);
		final ArgumentCaptor<Throwable> captor2 = ArgumentCaptor
		        .forClass(Throwable.class);
		handler.handle(this.logger, () -> testMessage, null);
		verify(Mockito.verify(this.logger, Mockito.times(count)), captor,
		        captor2);
		return count == 0 ? null : captor.getValue();
	}

	protected abstract void enable(boolean enable);

	protected abstract LoggingHandler getLoggingHandler();

	protected abstract void verify(Logger verify, ArgumentCaptor<String> captor,
	        ArgumentCaptor<Throwable> captor2);
}

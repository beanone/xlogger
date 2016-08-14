package org.beanone.xlogger.configure.impl;

import org.beanone.xlogger.LoggerLevel;
import org.beanone.xlogger.mock.MockException;
import org.junit.Assert;
import org.junit.Test;

public class ExceptionConfigHandlerTest {
	@Test
	public void testGetConfiguration() {
		final ExceptionConfigHandler handler = new ExceptionConfigHandler();
		Assert.assertEquals(LoggerLevel.ERROR,
		        handler.getConfiguration(new MockException()));
		handler.addConfigEntry(
		        "xlogger.exception." + MockException.class.getName(), "WARN");
		Assert.assertEquals(LoggerLevel.WARN,
		        handler.getConfiguration(new MockException()));
	}

	@Test
	public void testGetPrefix() {
		Assert.assertNotNull(new ExceptionConfigHandler().getPrefix());
	}

}

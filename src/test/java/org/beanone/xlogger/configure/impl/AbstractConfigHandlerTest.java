package org.beanone.xlogger.configure.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;

public class AbstractConfigHandlerTest {
	@SuppressWarnings("rawtypes")
	private class MyConfigHandler extends AbstractConfigHandler {
		private final String prefix;

		public MyConfigHandler(String prefix) {
			this.prefix = prefix;
		}

		@Override
		public Object getConfiguration(Object type) {
			return null;
		}

		@Override
		public String getPrefix() {
			return this.prefix;
		}

		@Override
		protected void configDefault(String key, String value) {
			AbstractConfigHandlerTest.this.result.put(key, value);
			AbstractConfigHandlerTest.this.configDefaultCalled.set(true);
		}

		@Override
		protected void configure(String key, String value) {
			AbstractConfigHandlerTest.this.result.put(key, value);
			AbstractConfigHandlerTest.this.configCalled.set(true);
		}

	}

	private final Map<String, String> result = new HashMap<>();
	private final AtomicBoolean configDefaultCalled = new AtomicBoolean();
	private final AtomicBoolean configCalled = new AtomicBoolean();

	@Test
	public void testAddConfigEntry() {
		final String key = "xlogger.ns.domain.*";
		Assert.assertFalse(this.configDefaultCalled.get());
		new MyConfigHandler("xlogger.ns.").addConfigEntry(key, "ERROR");
		Assert.assertTrue(this.configDefaultCalled.get());
		Assert.assertEquals("ERROR", this.result.get("domain"));
	}

	@Test
	public void testAddConfigEntryForMethod() {
		final String key = "xlogger.method.*";
		Assert.assertFalse(this.configDefaultCalled.get());
		new MyConfigHandler("xlogger.method.").addConfigEntry(key, "ERROR");
		Assert.assertTrue(this.configDefaultCalled.get());
		Assert.assertEquals("ERROR", this.result.get("*"));
	}

	@Test
	public void testConfigure() {
		final String key = "xlogger.ns.domain.public";
		Assert.assertFalse(this.configCalled.get());
		new MyConfigHandler("xlogger.ns.").addConfigEntry(key, "ERROR");
		Assert.assertTrue(this.configCalled.get());
		Assert.assertEquals("ERROR", this.result.get("domain.public"));
	}

}

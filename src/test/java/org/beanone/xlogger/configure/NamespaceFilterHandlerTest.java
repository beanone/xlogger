package org.beanone.xlogger.configure;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.beanone.xlogger.configure.NamespaceFilterHandler;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceFilterHandlerTest {

	@Test
	public void testAddConfigEntry() {
		final NamespaceFilterHandler handler = new NamespaceFilterHandler();
		Assert.assertNull(handler.getConfiguration("client"));
		handler.addConfigEntry("filter.client.1",
		        "package,org.beanone.xlogger.mock.client");
		final List<String> configs = handler.getConfiguration("client");
		Assert.assertNotNull(configs);
		Assert.assertEquals(1, configs.size());
	}

	@Test
	public void testProcessConfiguration() {
		final NamespaceFilterHandler handler = new NamespaceFilterHandler();
		handler.addConfigEntry("filter.client.1",
		        "package,org.beanone.xlogger.mock.client");
		handler.addConfigEntry("filter.client.2",
		        "class,org.beanone.xlogger.mock2.client.SomeRestClient");
		handler.addConfigEntry("filter.client.3",
		        "class,org.beanone.xlogger.mock3.client.SomeOtherClient.callThis");
		handler.addConfigEntry("filter.client",
		        "package,org.beanone.xlogger.mock3.client.SomeOtherClient.callThat");
		final AtomicInteger count = new AtomicInteger();
		handler.processConfiguration((ns, configurations) -> {
			if ("client".equals(ns) && configurations.size() == 4) {
				count.incrementAndGet();
			}
		});

		Assert.assertEquals(1, count.get());
	}

}

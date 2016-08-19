package org.beanone.xlogger;

import java.util.UUID;

import org.beanone.xlogger.mock.MockArg;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentSpecRegistryTest {

	@Test
	public void testCurrentGetSpecRegister() {
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry.current();
		Assert.assertNotNull(registry);
		Assert.assertNotNull(registry.getSpec(Integer.class));
		registry.register(MockArg.class, arg -> arg.getId());
		Assert.assertEquals("1",
		        registry.getSpec(MockArg.class).describe(new MockArg()));
	}

	@Test
	public void testCurrentString() {
		final String profile = "part";
		ArgumentSpecRegistry.initProfile(profile);
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry
		        .current(profile);
		Assert.assertNotNull(registry);
		Assert.assertNotNull(registry.getSpec(Integer.class));
		registry.register(MockArg.class, arg -> arg.getId());
		Assert.assertEquals("1",
		        registry.getSpec(MockArg.class).describe(new MockArg()));
	}

	@Test
	public void testCurrentStringDefault() {
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry
		        .current(UUID.randomUUID().toString());
		Assert.assertNull(registry);
	}

	@Test
	public void testCurrentStringWithoutInit() {
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry.current(" ");
		Assert.assertNotNull(registry);
	}
}

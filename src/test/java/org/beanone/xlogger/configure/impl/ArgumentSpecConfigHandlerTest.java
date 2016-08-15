package org.beanone.xlogger.configure.impl;

import org.beanone.xlogger.ArgumentSpec;
import org.beanone.xlogger.ArgumentSpecRegistry;
import org.beanone.xlogger.ConfiguredArgumentSpec;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentSpecConfigHandlerTest {

	@Test(expected = IllegalArgumentException.class)
	public void testAddConfigEntryBadClassName() {
		final ArgumentSpecConfigHandler handler = new ArgumentSpecConfigHandler();
		handler.addConfigEntry("spec..a.bogus.class.Name", "id");
	}

	@Test
	public void testAddConfigEntryDefaultPartition() {
		class ArgClass {
		}
		ArgumentSpec<?> result = ArgumentSpecRegistry.current()
		        .getSpec(ArgClass.class);
		Assert.assertFalse(result instanceof ConfiguredArgumentSpec);
		final ArgumentSpecConfigHandler handler = new ArgumentSpecConfigHandler();
		handler.addConfigEntry("spec.." + ArgClass.class.getName(), "id");
		result = ArgumentSpecRegistry.current().getSpec(ArgClass.class);
		Assert.assertTrue(result instanceof ConfiguredArgumentSpec);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddConfigEntryEmptyClassName() {
		final ArgumentSpecConfigHandler handler = new ArgumentSpecConfigHandler();
		handler.addConfigEntry("spec.part.", "id");
	}

	@Test
	public void testAddConfigEntryNamedPartition() {
		class AnotherArg {
		}
		ArgumentSpecRegistry.initPartition("verbose");
		ArgumentSpec<?> result = ArgumentSpecRegistry.current("verbose")
		        .getSpec(AnotherArg.class);
		Assert.assertFalse(result instanceof ConfiguredArgumentSpec);
		final ArgumentSpecConfigHandler handler = new ArgumentSpecConfigHandler();
		handler.addConfigEntry("spec.verbose." + AnotherArg.class.getName(),
		        "id,   attribute");
		result = ArgumentSpecRegistry.current("verbose")
		        .getSpec(AnotherArg.class);
		Assert.assertTrue(result instanceof ConfiguredArgumentSpec);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetConfiguration() {
		new ArgumentSpecConfigHandler().getConfiguration("someKey");
	}

	@Test
	public void testGetPrefix() {
		Assert.assertNotNull(new ArgumentSpecConfigHandler().getPrefix());
	}
}

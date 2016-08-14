package org.beanone.xlogger;

import org.beanone.xlogger.mock.MockArg;
import org.junit.Assert;
import org.junit.Test;

public class ConfiguredArgumentSpecTest {
	@Test
	public void testDescribe() {
		final ArgumentSpec<?> spec = new ConfiguredArgumentSpec("", "id");
		Assert.assertEquals("{id=1}", spec.asString(new MockArg()));
	}

	@Test
	public void testDescribeWithBadConfiguration() {
		final ArgumentSpec<?> spec = new ConfiguredArgumentSpec("",
		        "id, nonexistentattribute");
		Assert.assertEquals("{id=1}", spec.asString(new MockArg()));
	}
}

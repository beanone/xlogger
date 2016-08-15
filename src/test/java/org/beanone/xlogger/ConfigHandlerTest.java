package org.beanone.xlogger;

import org.beanone.xlogger.ConfigHandler;
import org.beanone.xlogger.LoggerLevel;
import org.beanone.xlogger.MethodAccessLevel;
import org.junit.Assert;
import org.junit.Test;

public class ConfigHandlerTest {
	private final ConfigHandler<String, String> handler = new ConfigHandler<String, String>() {

		@Override
		public void addConfigEntry(String key, String value) {
			// do nothing
		}

		@Override
		public String getConfiguration(String type) {
			// do nothing
			return null;
		}

		@Override
		public String getPrefix() {
			return "my.prefix.";
		}

		@Override
		public void postConfigure() {
			// do nothing by default
		}
	};

	@Test
	public void testAccept() {
		Assert.assertTrue(this.handler.accept("my.prefix.ns.public"));
		Assert.assertFalse(this.handler.accept("my.prefix1.ns.public"));
		Assert.assertFalse(this.handler.accept("my.prefix"));
	}

	@Test
	public void testStripPrefix() {
		Assert.assertEquals("ns.public",
		        this.handler.stripPrefix("my.prefix.ns.public"));
	}

	@Test
	public void testToAccessLevel() {
		Assert.assertEquals(MethodAccessLevel.PACKAGE,
		        this.handler.toAccessLevel("package"));
		Assert.assertEquals(MethodAccessLevel.PACKAGE,
		        this.handler.toAccessLevel("PACKaGE"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToAccessLevelInvalid() {
		Assert.assertNull(this.handler.toAccessLevel("invalid"));
	}

	@Test
	public void testToLoggerLevel() {
		Assert.assertEquals(LoggerLevel.ERROR,
		        this.handler.toLoggerLevel("error"));
		Assert.assertEquals(LoggerLevel.ERROR,
		        this.handler.toLoggerLevel("ERRoR"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLoggerLevelInvalid() {
		Assert.assertNull(this.handler.toLoggerLevel("invalid"));
	}

}

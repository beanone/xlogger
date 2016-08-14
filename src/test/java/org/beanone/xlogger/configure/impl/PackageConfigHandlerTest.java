package org.beanone.xlogger.configure.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.beanone.xlogger.AspectContext;
import org.beanone.xlogger.LoggerLevel;
import org.junit.Assert;
import org.junit.Test;

@Aspect
public class PackageConfigHandlerTest {
	@Around("execution (* *..*.PackageConfigHandlerTest.myPrivateMethod(..))")
	public ProceedingJoinPoint myAdvice(ProceedingJoinPoint pjp)
	        throws Throwable {
		return pjp;
	}

	@Test
	public void testGetConfigurationWithMatchingDefaulting() {
		final AspectContext context = toContext(myPrivateMethod());
		final PackageConfigHandler handler = new PackageConfigHandler();
		Assert.assertNull(handler.getConfiguration(context));
		handler.addConfigEntry("xlogger.package."
		        + this.getClass().getPackage().getName() + ".*", "INFO");
		Assert.assertEquals(LoggerLevel.INFO,
		        handler.getConfiguration(context));
	}

	@Test
	public void testGetConfigurationWithMatchingModifier() {
		final AspectContext context = toContext(myPrivateMethod());
		final PackageConfigHandler handler = new PackageConfigHandler();
		Assert.assertNull(handler.getConfiguration(context));
		handler.addConfigEntry("xlogger.package."
		        + this.getClass().getPackage().getName() + ".private", "DEBUG");
		Assert.assertEquals(LoggerLevel.DEBUG,
		        handler.getConfiguration(context));
	}

	@Test
	public void testGetConfigurationWithoutMatchingConfiguration() {
		final AspectContext context = toContext(myPrivateMethod());
		final PackageConfigHandler handler = new PackageConfigHandler();
		Assert.assertNull(handler.getConfiguration(context));
		handler.addConfigEntry("xlogger.package."
		        + this.getClass().getPackage().getName() + ".public", "INFO");
		Assert.assertNull(handler.getConfiguration(context));
	}

	@Test
	public void testGetPrefix() {
		Assert.assertNotNull(new PackageConfigHandler().getPrefix());
	}

	private ProceedingJoinPoint myPrivateMethod() {
		// do nothing
		return null;
	}

	private AspectContext toContext(JoinPoint point) {
		return new AspectContext(point, null);
	}

}

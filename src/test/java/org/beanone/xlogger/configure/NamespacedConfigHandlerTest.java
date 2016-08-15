package org.beanone.xlogger.configure;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.beanone.xlogger.AspectContext;
import org.beanone.xlogger.LoggerLevel;
import org.beanone.xlogger.configure.NamespaceFilterHandler;
import org.beanone.xlogger.configure.NamespacedConfigHandler;
import org.beanone.xlogger.mock.MockClass;
import org.junit.Assert;
import org.junit.Test;

@Aspect
public class NamespacedConfigHandlerTest {
	@Around("execution (* *..*.NamespacedConfigHandlerTest.*Method(..))")
	public ProceedingJoinPoint myAdvice(ProceedingJoinPoint pjp)
	        throws Throwable {
		return pjp;
	}

	public ProceedingJoinPoint myPublicMethod() {
		// do nothing
		return null;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddConfigEntryBadConfiguration() {
		final NamespacedConfigHandler handler = new NamespacedConfigHandler();
		handler.addConfigEntry("xlogger.ns.hello.badmodifier", "INFO");
	}

	@Test
	public void testGetConfiguration() {
		final NamespacedConfigHandler handler = new NamespacedConfigHandler();
		handler.addConfigEntry("xlogger.ns.domain.private", "DEBUG");
		handler.getRegister("domain")
		        .registerClassCondition(c -> c.equals(this.getClass()));
		Assert.assertEquals(LoggerLevel.DEBUG,
		        handler.getConfiguration(toContext(myPrivateMethod())));
	}

	@Test
	public void testGetConfigurationWithDefaulting() {
		final NamespacedConfigHandler handler = new NamespacedConfigHandler();
		handler.addConfigEntry("xlogger.ns.domain.*", "DEBUG");
		handler.getRegister("domain")
		        .registerClassCondition(c -> c.equals(this.getClass()));
		Assert.assertEquals(LoggerLevel.DEBUG,
		        handler.getConfiguration(toContext(myPrivateMethod())));
	}

	@Test
	public void testGetConfigurationWithoutMatchingAccessLevel() {
		final NamespacedConfigHandler handler = new NamespacedConfigHandler();
		handler.addConfigEntry("xlogger.ns.domain.public", "DEBUG");
		handler.getRegister("domain")
		        .registerClassCondition(c -> c.equals(this.getClass()));
		Assert.assertNull(
		        handler.getConfiguration(toContext(myPrivateMethod())));
	}

	@Test
	public void testGetPrefix() {
		Assert.assertNotNull(new NamespacedConfigHandler().getPrefix());
	}

	@Test
	public void testGetRegister() {
		final NamespacedConfigHandler handler = new NamespacedConfigHandler();
		handler.addConfigEntry("xlogger.ns.domain.private", "DEBUG");
		Assert.assertNotNull(handler.getRegister("domain"));
	}

	@Test
	public void testPostConfigureClassCondition() {
		testPostConfigure(true, myPrivateMethod(), "filter.domain",
		        LoggerLevel.DEBUG, "class," + this.getClass().getName());
	}

	@Test
	public void testPostConfigureDomainConfigDefaultingWithoutWildcard() {
		testPostConfigure(true, myPublicMethod(), "filter.important",
		        LoggerLevel.WARN,
		        "method," + this.getClass().getName() + ".myPublicMethod");
	}

	@Test
	public void testPostConfigureInvalidCondition() {
		testPostConfigure(false, myPrivateMethod(), "filter.domain",
		        LoggerLevel.DEBUG,
		        "bogus," + this.getClass().getPackage().getName());
	}

	@Test
	public void testPostConfigureMethodCondition() {
		testPostConfigure(true, myPrivateMethod(), "filter.domain",
		        LoggerLevel.DEBUG,
		        "method," + this.getClass().getName() + ".myPrivateMethod");
	}

	@Test
	public void testPostConfigurePackageCondition() {
		testPostConfigure(true, myPrivateMethod(), "filter.domain",
		        LoggerLevel.DEBUG,
		        "package," + this.getClass().getPackage().getName());
	}

	@Test
	public void testPostConfigurePackageConditionWithOtherConfigurations() {
		testPostConfigure(true, myPublicMethod(), "filter.domain",
		        LoggerLevel.INFO, "class," + MockClass.class.getName(),
		        "method," + this.getClass().getName() + ".myPublicMethod");
	}

	private ProceedingJoinPoint myPrivateMethod() {
		// do nothing
		return null;
	}

	private void testPostConfigure(boolean foundLogger, JoinPoint point,
	        String filterPrefix, LoggerLevel expected,
	        String... configurations) {
		final NamespacedConfigHandler handler = new NamespacedConfigHandler();
		handler.addConfigEntry("xlogger.ns.domain.public", "INFO");
		handler.addConfigEntry("xlogger.ns.domain.*", "DEBUG");
		handler.addConfigEntry("xlogger.ns.important", "WARN");
		final NamespaceFilterHandler filterHandler = new NamespaceFilterHandler();
		handler.setFilterHandler(filterHandler);
		handler.postConfigure();
		Assert.assertNull(handler.getConfiguration(toContext(point)));
		for (int i = 0; i < configurations.length; i++) {
			filterHandler.addConfigEntry(filterPrefix + '.' + i,
			        configurations[i]);
		}
		handler.postConfigure();
		if (foundLogger) {
			Assert.assertEquals(expected,
			        handler.getConfiguration(toContext(point)));
		} else {
			Assert.assertNull(handler.getConfiguration(toContext(point)));
		}
	}

	private AspectContext toContext(JoinPoint point) {
		return new AspectContext(point, null);
	}

}

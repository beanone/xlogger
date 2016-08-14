package org.beanone.xlogger.configure.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.beanone.xlogger.AspectContext;
import org.beanone.xlogger.LoggerLevel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:xlogger-test-context.xml" })
@Aspect
public class MethodConfigHandlerTest {
	@Autowired
	private MethodConfigHandler methodHandler;

	@Autowired
	private NamespacedConfigHandler namespaceHandler;

	@Autowired
	private PackageConfigHandler packageHandler;

	@Around("execution (* *..*.MethodConfigHandlerTest.*Method(..))")
	public ProceedingJoinPoint myAdvice(ProceedingJoinPoint pjp)
	        throws Throwable {
		return pjp;
	}

	public ProceedingJoinPoint myPublicMethod() {
		// do nothing
		return null;
	}

	@Test
	public void testGetConfigurationPackage() {
		resetAll();
		setupMethodLogger();
		Assert.assertEquals(LoggerLevel.TRACE, this.methodHandler
		        .getConfiguration(toContext(myPackageMethod())));
	}

	@Test
	public void testGetConfigurationPrivate() {
		resetAll();
		setupMethodLogger();
		Assert.assertEquals(LoggerLevel.SKIP, this.methodHandler
		        .getConfiguration(toContext(myPrivateMethod())));
	}

	@Test
	public void testGetConfigurationPublic() {
		resetAll();
		setupMethodLogger();
		Assert.assertEquals(LoggerLevel.INFO, this.methodHandler
		        .getConfiguration(toContext(myPublicMethod())));
	}

	@Test
	public void testGetPrefix() {
		Assert.assertNotNull(new MethodConfigHandler().getPrefix());
	}

	@Test
	public void testNamespaceHandler() {
		Assert.assertNotNull(this.methodHandler.getNamespaceHandler());
	}

	@Test
	public void testPackageHandler() {
		Assert.assertNotNull(this.methodHandler.getPackageHandler());
	}

	@Test
	public void testRegisterClassCondition() {
		resetAll();
		setupDomainLogger();

		// make sure the we are tolerant to bogus configuration
		this.methodHandler.registerClassCondition("bogus",
		        c -> c.equals("bogus"));

		this.methodHandler.registerClassCondition("important",
		        c -> c.equals(this.getClass()));
		Assert.assertEquals(LoggerLevel.INFO, this.methodHandler
		        .getConfiguration(toContext(myPackageMethod())));
	}

	@Test
	public void testRegisterJoinPointCondition() {
		resetAll();
		setupDomainLogger();
		final JoinPoint point = myPackageMethod();

		// make sure the we are tolerant to bogus configuration
		this.methodHandler.registerJoinPointCondition("bogus", p -> p == point);

		this.methodHandler.registerJoinPointCondition("trivial",
		        p -> p == point);
		Assert.assertEquals(LoggerLevel.DEBUG,
		        this.methodHandler.getConfiguration(toContext(point)));
	}

	@Test
	public void testRegisterMethodCondition() {
		resetAll();
		setupDomainLogger();
		setupPackageLogger();

		// make sure the we are tolerant to bogus configuration
		this.methodHandler.registerMethodCondition("bogus",
		        m -> m.getName().equals("bogusMethod"));

		this.methodHandler.registerMethodCondition("important",
		        m -> m.getName().equals("myPublicMethod"));
		Assert.assertEquals(LoggerLevel.INFO, this.methodHandler
		        .getConfiguration(toContext(myPublicMethod())));
		Assert.assertEquals(LoggerLevel.WARN, this.methodHandler
		        .getConfiguration(toContext(myPackageMethod())));
		Assert.assertEquals(LoggerLevel.DEBUG, this.methodHandler
		        .getConfiguration(toContext(myPrivateMethod())));
	}

	@Test
	public void testRegisterPackageCondition() {
		resetAll();
		setupDomainLogger();

		// make sure the we are tolerant to bogus configuration
		this.methodHandler.registerPackageCondition("bogus",
		        p -> p.equals("bogus"));

		this.methodHandler.registerPackageCondition("trivial",
		        p -> p.equals(this.getClass().getPackage()));
		Assert.assertEquals(LoggerLevel.DEBUG, this.methodHandler
		        .getConfiguration(toContext(myPackageMethod())));
	}

	private ProceedingJoinPoint myPrivateMethod() {
		// do nothing
		return null;
	}

	private void resetAll() {
		this.namespaceHandler.reset();
		this.methodHandler.reset();
		this.packageHandler.reset();
	}

	private void setupDomainLogger() {
		this.namespaceHandler.reset();
		this.namespaceHandler.addConfigEntry("xlogger.ns.important", "info");
		this.namespaceHandler.addConfigEntry("xlogger.ns.trivial.package",
		        "DEBUG");
	}

	private void setupMethodLogger() {
		this.methodHandler.reset();
		this.methodHandler.addConfigEntry("xlogger.method.public", "INfo");
		this.methodHandler.addConfigEntry("xlogger.method.private", "SKIP");
		this.methodHandler.addConfigEntry("xlogger.method.*", "TRACE");
	}

	private void setupPackageLogger() {
		this.packageHandler.reset();
		this.packageHandler.addConfigEntry("xlogger.package."
		        + this.getClass().getPackage().getName() + ".package", "warn");
		this.packageHandler.addConfigEntry("xlogger.package."
		        + this.getClass().getPackage().getName() + ".*", "dEbug");
	}

	private AspectContext toContext(JoinPoint point) {
		return new AspectContext(point, null);
	}

	ProceedingJoinPoint myPackageMethod() {
		// do nothing
		return null;
	}
}

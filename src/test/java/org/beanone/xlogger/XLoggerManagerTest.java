package org.beanone.xlogger;

import java.io.InputStream;
import java.util.Collection;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.beanone.xlogger.configure.impl.MethodConfigHandler;
import org.beanone.xlogger.configure.impl.NamespacedConfigHandler;
import org.beanone.xlogger.configure.impl.PackageConfigHandler;
import org.beanone.xlogger.mock.MockException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:xlogger-test-context.xml" })
@Aspect
public class XLoggerManagerTest {
	@Autowired
	private MethodConfigHandler methodHandler;

	@Autowired
	private NamespacedConfigHandler namespaceHandler;

	@Autowired
	private PackageConfigHandler packageHandler;

	@Autowired
	private XLoggerManager manager;

	@Autowired
	private CacheManager cacheManager;

	@Around("execution (* *..*.XLoggerManagerTest.*Method(..))")
	public ProceedingJoinPoint myAdvice(ProceedingJoinPoint pjp)
	        throws Throwable {
		return pjp;
	}

	public ProceedingJoinPoint myPublicMethod() {
		// do nothing
		return null;
	}

	@Around("execution (* *..*.XLoggerManagerTest.get*(..))")
	public ProceedingJoinPoint myTrivialMethods(ProceedingJoinPoint pjp)
	        throws Throwable {
		return pjp;
	}

	@Before
	public void setup() throws Exception {
		resetAll();
		final InputStream stream = this.getClass()
		        .getResourceAsStream("/xlogger.properties");
		Assert.assertNotNull(stream);
		this.manager.init(stream);
	}

	@Test
	public void testGetExceptionLoggerLevel() throws Exception {
		Assert.assertEquals(LoggerLevel.WARN,
		        this.manager.getExceptionLoggerLevel(
		                new AspectContext(null, new MockException())));
		Assert.assertEquals(LoggerLevel.ERROR,
		        this.manager.getExceptionLoggerLevel(
		                new AspectContext(null, new Exception())));
	}

	@Test
	public void testGetMethodLoggerLevel() throws Exception {
		Assert.assertEquals(LoggerLevel.INFO, this.manager.getMethodLoggerLevel(
		        new AspectContext(myPublicMethod(), null)));
		Assert.assertEquals(LoggerLevel.DEBUG,
		        this.manager.getMethodLoggerLevel(
		                new AspectContext(myPackageMethod(), null)));
		Assert.assertEquals(LoggerLevel.SKIP, this.manager.getMethodLoggerLevel(
		        new AspectContext(myPrivateMethod(), null)));
	}

	@Test
	public void testGetNamespaceSupporter() {
		Assert.assertNotNull(this.manager.getNamespaceSupporter());
	}

	@Test
	public void testNamespaceConfiguration() {
		final JoinPoint point = getJointPoint();
		Assert.assertEquals(LoggerLevel.DEBUG, this.manager
		        .getMethodLoggerLevel(new AspectContext(point, null)));
		this.manager.getNamespaceSupporter()
		        .registerJoinPointCondition("trivial", p -> p == point);
		clearCaches();
		Assert.assertEquals(LoggerLevel.SKIP, this.manager
		        .getMethodLoggerLevel(new AspectContext(point, null)));
	}

	private void clearCaches() {
		final Collection<String> names = this.cacheManager.getCacheNames();
		names.forEach(n -> this.cacheManager.getCache(n).clear());
	}

	private ProceedingJoinPoint myPrivateMethod() {
		// do nothing
		return null;
	}

	private void resetAll() {
		this.namespaceHandler.reset();
		this.methodHandler.reset();
		this.packageHandler.reset();
		clearCaches();
	}

	ProceedingJoinPoint getJointPoint() {
		return null;
	}

	ProceedingJoinPoint myPackageMethod() {
		// do nothing
		return null;
	}
}

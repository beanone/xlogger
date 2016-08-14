package org.beanone.xlogger;

import java.io.InputStream;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.beanone.xlogger.mock.MockException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:xlogger-test-context.xml" })
@Aspect
public class XLoggerManagerTest {
	@Autowired
	private XLoggerManager manager;

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

	@Test
	public void testGetExceptionLoggerLevel() throws Exception {
		Assert.assertEquals(LoggerLevel.WARN,
		        this.manager.getExceptionLoggerLevel(new MockException()));
		Assert.assertEquals(LoggerLevel.ERROR,
		        this.manager.getExceptionLoggerLevel(new Exception()));
	}

	@Test
	public void testGetMethodLoggerLevel() throws Exception {
		Assert.assertEquals(LoggerLevel.INFO,
		        this.manager.getMethodLoggerLevel(myPublicMethod()));
		Assert.assertEquals(LoggerLevel.DEBUG,
		        this.manager.getMethodLoggerLevel(myPackageMethod()));
		Assert.assertEquals(LoggerLevel.SKIP,
		        this.manager.getMethodLoggerLevel(myPrivateMethod()));
	}

	@Test
	public void testGetNamespaceSupporter() {
		Assert.assertNotNull(this.manager.getNamespaceSupporter());
	}

	@Test
	public void testInit() throws Exception {
		final InputStream stream = this.getClass()
		        .getResourceAsStream("/xlogger.properties");
		Assert.assertNotNull(stream);
		this.manager.init(stream);
	}

	@Test
	public void testNamespaceConfiguration() {
		final JoinPoint point = getJointPoint();
		Assert.assertEquals(LoggerLevel.DEBUG,
		        this.manager.getMethodLoggerLevel(point));
		this.manager.getNamespaceSupporter()
		        .registerJoinPointCondition("trivial", p -> p == point);
		Assert.assertEquals(LoggerLevel.SKIP,
		        this.manager.getMethodLoggerLevel(point));
	}

	private ProceedingJoinPoint myPrivateMethod() {
		// do nothing
		return null;
	}

	ProceedingJoinPoint getJointPoint() {
		return null;
	}

	ProceedingJoinPoint myPackageMethod() {
		// do nothing
		return null;
	}
}

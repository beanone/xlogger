package org.beanone.xlogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.beanone.xlogger.mock.MockArg;
import org.beanone.xlogger.mock.MockClass;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerSupportTest {

	@Test
	public void testDoLogWithArguments() {
		final TestResult result = new TestResult();
		final LoggingHandler handler = (logger, snf,
		        t) -> result.result = snf.asString();
		final Logger logger = LoggerFactory.getLogger(LoggerSupportTest.class);
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry.current();
		LoggerSupport.doLog(new LoggingContext().tag("Enter")
		        .methodName("amethod").handler(handler).logger(logger)
		        .registry(registry).names(new String[0]).args(new Object[0]));
		Assert.assertEquals("Enter amethod()", result.result);
	}

	@Test
	public void testDoLogWithoutArgumentNoNull() {
		final TestResult result = new TestResult();
		final LoggingHandler handler = (logger, snf,
		        t) -> result.result = snf.asString();
		final Logger logger = LoggerFactory.getLogger(LoggerSupportTest.class);
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry.current();
		LoggerSupport
		        .doLog(new LoggingContext().tag("Enter").methodName("amethod")
		                .handler(handler).logger(logger).registry(registry)
		                .names(new String[] { "arg1", "arg2", "arg3" })
		                .args(new Object[] { 1, "2", true }));
		Assert.assertEquals("Enter amethod(arg1=1, arg2=2, arg3=true)",
		        result.result);
	}

	@Test
	public void testDoLogWithoutArgumentSomeNull() {
		final TestResult result = new TestResult();
		final LoggingHandler handler = (logger, snf,
		        t) -> result.result = snf.asString();
		final Logger logger = LoggerFactory.getLogger(LoggerSupportTest.class);
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry.current();
		LoggerSupport
		        .doLog(new LoggingContext().tag("Enter").methodName("amethod")
		                .handler(handler).logger(logger).registry(registry)
		                .names(new String[] { "arg1", "arg2", "arg3" })
		                .args(new Object[] { 1, null, true }));
		Assert.assertEquals("Enter amethod(arg1=1, arg2, arg3=true)",
		        result.result);
	}

	@Test
	public void testGetExceptionLevelThrowableNull() {
		Assert.assertSame(LoggerLevel.ERROR, LoggerSupport.getExceptionLevel(
		        new ExceptionSpec[0], null, LoggerLevel.ERROR));
	}

	@Test
	public void testGetExceptionLevelWithMatchedExceptionSpec() {
		Assert.assertSame(LoggerLevel.WARN,
		        LoggerSupport.getExceptionLevel(createExceptionLevels(),
		                new IllegalArgumentException(), LoggerLevel.ERROR));

	}

	@Test
	public void testGetExceptionLevelWithUnmatchedExceptionSpec() {
		Assert.assertSame(LoggerLevel.ERROR, LoggerSupport.getExceptionLevel(
		        createExceptionLevels(), new Exception(), LoggerLevel.ERROR));

	}

	@Test
	public void testGetLoggerSpecForMethodAnnotated() throws Exception {
		final Method method = MockClass.class.getMethod("methodWithAnotation");
		Assert.assertNotNull(LoggerSupport.getLoggerSpec(method));
		final Method method1 = MockClass.class.getMethod(
		        "methodWithArgWithAnotation", new Class[] { MockArg.class });
		Assert.assertNotNull(LoggerSupport.getLoggerSpec(method1));
	}

	@Test
	public void testGetLoggerSpecForMethodNotAnnotated() throws Exception {
		final Method method = MockClass.class
		        .getMethod("methodWithoutAnotation");
		Assert.assertNotNull(LoggerSupport.getLoggerSpec(method));
		final Method method1 = MockClass.class.getMethod(
		        "methodWithArgWithoutAnotation", new Class[] { MockArg.class });
		Assert.assertNotNull(LoggerSupport.getLoggerSpec(method1));
	}

	protected ExceptionSpec[] createExceptionLevels() {
		return new ExceptionSpec[] { new ExceptionSpec() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return ExceptionSpec.class;
			}

			@Override
			public Class<? extends Throwable> exception() {
				return IllegalArgumentException.class;
			}

			@Override
			public LoggerLevel level() {
				return LoggerLevel.WARN;
			}
		} };
	}
}

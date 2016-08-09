package org.beanone.xlogger.mock.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.beanone.xlogger.AbstractMethodLogger;
import org.beanone.xlogger.LoggerLevel;
import org.beanone.xlogger.LoggerSpec;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A reference implementation of the aspect used by the Unit tests.
 *
 * @author Hongyan Li
 *
 */
@Aspect
public class MockMethodLogger extends AbstractMethodLogger {
	private static Logger LOGGER = LoggerFactory
	        .getLogger(MockMethodLogger.class);
	public static Logger MOCK_LOGGER = Mockito.spy(LOGGER);

	/**
	 * This pointcut represent the control flow of invoking any method of
	 * classes in this package.
	 */
	@Pointcut("cflow(call(* org.beanone.xlogger.mock.aspect.*.*(..)))")
	public void inAspect() {
	}

	@Pointcut("within(*Test)")
	public void inTest() {

	}

	/**
	 * An around advice to methods for logging of exception thrown from methods.
	 * The default logging level is ERROR. A different advice can be defined to
	 * use a different default logging level. More fine control on the logging
	 * level can be achieved by annotating the method of concern with
	 * {@link LoggerSpec}.
	 *
	 * @param point
	 *            the pointcut this advice is for.
	 * @param t
	 *            the exception thrown by the method.
	 */
	@AfterThrowing(pointcut = "xloggerMock() && !inAspect() && !inFramework() && !trivial() && !inTest()", throwing = "t")
	public void logException(JoinPoint point, Throwable t) {
		handleThrows(point, t, null, LoggerLevel.ERROR);
	}

	/**
	 * An around advice to methods for logging of access to methods. The default
	 * logging level is TRACE. A different advice can be defined to use a
	 * different default logging level. More fine control on the logging level
	 * can be achieved by annotating the method of concern with
	 * {@link LoggerSpec} .
	 *
	 * @param point
	 *            the pointcut this advice is for.
	 * @return the result of invoking the advised method.
	 * @throws Throwable
	 *             the exception thrown by the method advised.
	 */
	@Around("xloggerMock() && !inAspect() && !inFramework() && !trivial() && !inTest()")
	public Object logInvocation(ProceedingJoinPoint point) throws Throwable {
		return handle(point, LoggerLevel.TRACE);
	}

	/**
	 * This pointcut represents the execution of any getter and setter method.
	 */
	@Pointcut("execution(* *..*.get*(..)) || execution(* *..*.set*(..)) || execution(* *..*.is*(..)) || execution(* *..*.add*(..))")
	public void trivial() {

	}

	/**
	 * This pointcut represents the execution of any method of classes in the
	 * mock package.
	 */
	@Pointcut("execution(* org.beanone.xlogger.mock.*.*(..))")
	public void xloggerMock() {
	}

	/**
	 * return the mocked Logger so that we can count how many times the logging
	 * method is invoked.
	 */
	@Override
	protected Logger getLogger(Class<?> clazz) {
		super.getLogger(clazz).trace("Test");
		Mockito.when(MOCK_LOGGER.isTraceEnabled()).thenReturn(true);
		return MOCK_LOGGER;
	}
}

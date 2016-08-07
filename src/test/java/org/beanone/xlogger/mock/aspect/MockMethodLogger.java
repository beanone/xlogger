package org.beanone.xlogger.mock.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.beanone.xlogger.AbstractMethodLogger;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class MockMethodLogger extends AbstractMethodLogger {
	private static Logger LOGGER = LoggerFactory
	        .getLogger(MockMethodLogger.class);
	public static Logger MOCK_LOGGER = Mockito.spy(LOGGER);

	@Around("xloggerMock() && !inAspect() && !inFramework() && !trivial()")
	public Object acound(ProceedingJoinPoint point) throws Throwable {
		return handle(point);
	}

	@Pointcut("cflow(call(* org.beanone.xlogger.mock.aspect.*.*(..)))")
	public void inAspect() {
	}

	@AfterThrowing(pointcut = "xloggerMock() && !inAspect() && !inFramework() && !trivial()", throwing = "t")
	public void throwing(JoinPoint point, Throwable t) throws Throwable {
		handleThrow(point, t);
	}

	@Pointcut("execution(* *..*.get*(..)) || execution(* *..*.set*(..)) || execution(* *..*.is*(..)) || execution(* *..*.add*(..))")
	public void trivial() {

	}

	@Pointcut("execution(* org.beanone.xlogger.mock.*.*(..))")
	public void xloggerMock() {
	}

	@Override
	protected Logger getLogger(Object invoker) {
		super.getLogger(invoker);
		Mockito.when(MOCK_LOGGER.isTraceEnabled()).thenReturn(true);
		return MOCK_LOGGER;
	}
}

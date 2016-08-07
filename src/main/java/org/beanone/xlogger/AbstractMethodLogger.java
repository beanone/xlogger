package org.beanone.xlogger;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract based implementation of a method logging aspect. To use this, extend
 * it and define your joint point and call the handle method.
 *
 * @author Hongyan Li
 *
 */
@Aspect
public abstract class AbstractMethodLogger {
	private static final Logger LOGGER = LoggerFactory
	        .getLogger(AbstractMethodLogger.class);

	@Pointcut("!execution(* org.beanone.xlogger.*.*(..))")
	public void notFramework() {
	}

	private Method getMethod(JoinPoint pjp) throws NoSuchMethodException {
		Method method = null;
		if (pjp.getSignature() instanceof MethodSignature) {
			final MethodSignature signature = (MethodSignature) pjp
			        .getSignature();
			method = signature.getMethod();
			if (method.getDeclaringClass().isInterface()) {
				method = pjp.getTarget().getClass().getDeclaredMethod(
				        signature.getName(), method.getParameterTypes());
			}
		}
		return method;
	}

	protected Logger getLogger(final Object invoker) {
		return LoggerFactory.getLogger(invoker.getClass());
	}

	protected Object handle(ProceedingJoinPoint pjp) throws Throwable {
		final Object invoker = pjp.getThis();
		final Logger logger = getLogger(invoker);
		final Method method = getMethod(pjp);
		final LoggerSpec spec = LoggerSupport.getLoggerSpec(method);
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry
		        .current(spec.partition());
		final Signature signature = pjp.getStaticPart().getSignature();

		if (signature instanceof MethodSignature) {
			final MethodSignature ms = (MethodSignature) signature;
			final String[] params = ms.getParameterNames();
			final Object[] args = pjp.getArgs();
			final LoggerLevel level = spec.level();
			LoggerSupport.doLog("Enter", method.getName(), level, logger,
			        registry, null, params, args);
			final Object returns = pjp.proceed(args);
			if (returns == null) {
				LoggerSupport.doLog("Exit", method.getName(), level, logger,
				        registry, null, new String[0], new Object[0]);
			} else {
				LoggerSupport.doLog("Exit", method.getName(), level, logger,
				        registry, null, new String[] { "result" },
				        new Object[] { returns });
			}
			return returns;
		} else {
			LOGGER.warn(
			        "MethodLogger error, method signature expected, but it was {}",
			        signature);
		}

		// not a method invocation and thus OK to return null
		return null;
	}

	protected void handleThrow(JoinPoint pjp, Throwable t) throws Throwable {
		final Object invoker = pjp.getThis();
		final Logger logger = getLogger(invoker);
		final Method method = getMethod(pjp);
		final LoggerSpec spec = LoggerSupport.getLoggerSpec(method);
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry
		        .current(spec.partition());
		final Signature signature = pjp.getStaticPart().getSignature();

		if (signature instanceof MethodSignature) {
			final MethodSignature ms = (MethodSignature) signature;
			final String[] params = ms.getParameterNames();
			final Object[] args = pjp.getArgs();
			final LoggerLevel level = spec.level();
			LoggerSupport.doLog("Enter", method.getName(), level, logger,
			        registry, t, params, args);
		}
	}
}

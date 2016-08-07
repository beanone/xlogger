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
		}
		return method;
	}

	private void logWrongUseOfAspect(final Signature signature) {
		// This should never happen since the Aspect should only be applied
		// to a method. However, if the user mistakenly applies this aspect
		// to something other than a method, we ignore it but log a warning
		// message.
		LOGGER.warn(
		        "MethodLogger error, apsect applicable to method long, but it was {}",
		        signature);
	}

	/**
	 * Fetches the {@link Logger} of the invoker.
	 *
	 * @param invoker
	 *            the method invoker object.
	 * @return a Logger instance. The subclasses can override this as needed.
	 */
	protected Logger getLogger(final Object invoker) {
		return LoggerFactory.getLogger(invoker.getClass());
	}

	protected Object handle(ProceedingJoinPoint pjp) throws Throwable {
		final Object invoker = pjp.getThis();
		final Method method = getMethod(pjp);
		final LoggerSpec spec = LoggerSupport.getLoggerSpec(method);
		final Signature signature = pjp.getStaticPart().getSignature();

		final LoggingContext context = new LoggingContext()
		        .logger(getLogger(invoker))
		        .registry(ArgumentSpecRegistry.current(spec.partition()));
		if (signature instanceof MethodSignature) {
			final MethodSignature ms = (MethodSignature) signature;
			final Object[] args = pjp.getArgs();
			context.methodName(method.getName()).handler(spec.level())
			        .names(ms.getParameterNames()).args(args);
			LoggerSupport.doLog(context.tag("Enter"));
			final Object returns = pjp.proceed(args);
			if (returns == null) {
				LoggerSupport.doLog(context.tag("Exit").names(new String[0])
				        .args(new Object[0]));
			} else {
				LoggerSupport.doLog(
				        context.tag("Exit").names(new String[] { "result" })
				                .args(new Object[] { returns }));
			}
			return returns;
		} else {
			logWrongUseOfAspect(signature);
		}

		// not a method invocation and thus OK to return null
		return null;
	}

	protected void handleThrow(JoinPoint pjp, Throwable t) throws Throwable {
		final Object invoker = pjp.getThis();
		final Method method = getMethod(pjp);
		final LoggerSpec spec = LoggerSupport.getLoggerSpec(method);
		final Signature signature = pjp.getStaticPart().getSignature();

		final LoggingContext context = new LoggingContext()
		        .logger(getLogger(invoker))
		        .registry(ArgumentSpecRegistry.current(spec.partition()));
		if (signature instanceof MethodSignature) {
			final MethodSignature ms = (MethodSignature) signature;
			final ExceptionSpec[] levels = spec.exceptionLevel();
			LoggerSupport
			        .doLog(context.tag("Exception").methodName(method.getName())
			                .handler(LoggerSupport.getExceptionLevel(levels, t))
			                .exception(t).names(ms.getParameterNames())
			                .args(pjp.getArgs()));
		} else {
			logWrongUseOfAspect(signature);
		}
	}
}

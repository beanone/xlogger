package org.beanone.xlogger;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;

/**
 * Holds on to the context data of a join point of an aspect.
 *
 * @author Hongyan Li
 *
 */
public class AspectContext {
	private final JoinPoint joinPoint;
	private final Throwable exception;
	private final Method method;
	private final MethodAccessLevel access;

	/**
	 * Create an instance from a {@link JoinPoint} an an Exception.
	 *
	 * @param point
	 *            a {@link JoinPoint}.
	 * @param exception
	 *            a {@link Exception} in an Exception {@link JoinPoint}.
	 */
	public AspectContext(JoinPoint point, Throwable exception) {
		this.joinPoint = point;
		this.exception = exception;
		this.method = ConfigUtils.getMethod(point);
		this.access = ConfigUtils.getAccessLevel(this.method);
	}

	public MethodAccessLevel getAccess() {
		return this.access;
	}

	public Throwable getException() {
		return this.exception;
	}

	public JoinPoint getJoinPoint() {
		return this.joinPoint;
	}

	public Method getMethod() {
		return this.method;
	}
}

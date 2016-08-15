package org.beanone.xlogger;

import java.lang.reflect.Method;
import java.util.function.Predicate;

import org.aspectj.lang.JoinPoint;

public interface NamespaceSupporter {

	/**
	 * Registers a class based condition with a namespace.
	 *
	 * @param ns
	 *            a namespace string.
	 * @param condition
	 *            a class based condition.
	 */
	public void registerClassCondition(String ns,
	        Predicate<Class<?>> condition);

	/**
	 * Registers a {@link JoinPoint} based condition with a namespace.
	 *
	 * @param ns
	 *            a namespace string.
	 * @param condition
	 *            a {@link JoinPoint} based condition.
	 */
	public void registerJoinPointCondition(String ns,
	        Predicate<JoinPoint> condition);

	/**
	 * Registers a method based condition with a namespace.
	 *
	 * @param ns
	 *            a namespace string.
	 * @param condition
	 *            a method based condition.
	 */
	public void registerMethodCondition(String ns, Predicate<Method> condition);

	/**
	 * Registers a package based condition with a namespace.
	 *
	 * @param ns
	 *            a namespace string.
	 * @param condition
	 *            a package based condition.
	 */
	public void registerPackageCondition(String ns,
	        Predicate<Package> condition);
}

package org.beanone.xlogger.configure.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.aspectj.lang.JoinPoint;
import org.beanone.xlogger.AspectContext;

/**
 * Register for matching conditions for namespaces.
 *
 * @author Hongyan Li
 *
 */
public class NamespaceRegister implements Predicate<AspectContext> {
	private final String namespace;
	private final List<Predicate<Method>> methodBased = new ArrayList<>();
	private final List<Predicate<Package>> packageBased = new ArrayList<>();
	private final List<Predicate<Class<?>>> classBased = new ArrayList<>();
	private final List<Predicate<JoinPoint>> joinPointBased = new ArrayList<>();

	/**
	 * Construct this for a namespace.
	 *
	 * @param namespace
	 *            the namespace this register is for.
	 */
	public NamespaceRegister(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespace() {
		return this.namespace;
	}

	/**
	 * Registers a class based condition.
	 *
	 * @param condition
	 *            a class based condition.
	 */
	public void registerClassCondition(Predicate<Class<?>> condition) {
		this.classBased.add(condition);
	}

	/**
	 * Registers a {@link JoinPoint} based condition.
	 *
	 * @param condition
	 *            a {@link JoinPoint} based condition.
	 */
	public void registerJoinPointCondition(Predicate<JoinPoint> condition) {
		this.joinPointBased.add(condition);
	}

	/**
	 * Registers a method based condition.
	 *
	 * @param condition
	 *            a method based condition.
	 */
	public void registerMethodCondition(Predicate<Method> condition) {
		this.methodBased.add(condition);
	}

	/**
	 * Registers a package based condition.
	 *
	 * @param condition
	 *            a package based condition.
	 */
	public void registerPackageCondition(Predicate<Package> condition) {
		this.packageBased.add(condition);
	}

	@Override
	public boolean test(AspectContext context) {
		final Method method = context.getMethod();
		return acceptJoinPoint(context.getJoinPoint())
		        || acceptClass(method.getDeclaringClass())
		        || acceptPackage(method.getDeclaringClass().getPackage())
		        || acceptMethod(method);
	}

	private <T> boolean accept(List<Predicate<T>> predicates, T target) {
		for (final Predicate<T> predicate : predicates) {
			if (predicate.test(target)) {
				return true;
			}
		}
		return false;
	}

	private boolean acceptClass(@SuppressWarnings("rawtypes") Class clazz) {
		return accept(this.classBased, clazz);
	}

	private boolean acceptJoinPoint(JoinPoint point) {
		return accept(this.joinPointBased, point);
	}

	private boolean acceptMethod(Method method) {
		return accept(this.methodBased, method);
	}

	private boolean acceptPackage(Package pkg) {
		return accept(this.packageBased, pkg);
	}
}

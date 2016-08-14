package org.beanone.xlogger.configure.impl;

import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.beanone.xlogger.AspectContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Aspect
public class NamespaceRegisterTest {
	private ProceedingJoinPoint point;

	@Around("execution (* *..*.NamespaceRegisterTest.myPrivateMethod(..))")
	public ProceedingJoinPoint myAdvice(ProceedingJoinPoint pjp)
	        throws Throwable {
		return pjp;
	}

	@Before
	public void setup() {
		this.point = myPrivateMethod();
	}

	@Test
	public void testGetNamespace() {
		final String ns = UUID.randomUUID().toString();
		Assert.assertEquals(ns, new NamespaceRegister(ns).getNamespace());
	}

	@Test
	public void testRegisterClassCondition() {
		final String ns = UUID.randomUUID().toString();
		final NamespaceRegister register = new NamespaceRegister(ns);
		Assert.assertFalse(register.test(getContext()));
		register.registerClassCondition(
		        c -> c.getName().equals(this.getClass().getName()));
		Assert.assertTrue(register.test(getContext()));
	}

	@Test
	public void testRegisterJoinPointCondition() {
		final String ns = UUID.randomUUID().toString();
		final NamespaceRegister register = new NamespaceRegister(ns);
		Assert.assertFalse(register.test(getContext()));
		register.registerJoinPointCondition(
		        c -> c.getKind().equals(getContext().getJoinPoint().getKind()));
		Assert.assertTrue(register.test(getContext()));
	}

	@Test
	public void testRegisterMethodCondition() {
		final String ns = UUID.randomUUID().toString();
		final NamespaceRegister register = new NamespaceRegister(ns);
		Assert.assertFalse(register.test(getContext()));
		register.registerMethodCondition(
		        m -> (m.getDeclaringClass().getName() + '.' + m.getName())
		                .equals(this.getClass().getName()
		                        + ".myPrivateMethod"));
		Assert.assertTrue(register.test(getContext()));
	}

	@Test
	public void testRegisterPackageCondition() {
		final String ns = UUID.randomUUID().toString();
		final NamespaceRegister register = new NamespaceRegister(ns);
		Assert.assertFalse(register.test(getContext()));
		register.registerPackageCondition(c -> c.getName()
		        .equals(this.getClass().getPackage().getName()));
		Assert.assertTrue(register.test(getContext()));
	}

	private ProceedingJoinPoint myPrivateMethod() {
		// do nothing
		return null;
	}

	protected AspectContext getContext() {
		return new AspectContext(this.point, null);
	}
}

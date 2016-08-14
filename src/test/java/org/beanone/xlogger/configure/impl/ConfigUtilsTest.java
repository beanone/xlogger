package org.beanone.xlogger.configure.impl;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.beanone.xlogger.MethodAccessLevel;
import org.junit.Assert;
import org.junit.Test;

@Aspect
public class ConfigUtilsTest {
	@Around("execution (* *..*.ConfigUtilsTest.myPrivateMethod(..))")
	public Method myAdvice(ProceedingJoinPoint pjp) throws Throwable {
		return ConfigUtils.getMethod(pjp);
	}

	public void myPublicMethod() {
		// do nothing
	}

	@Test
	public void testGetAccessLevelPackage() throws Exception {
		Assert.assertEquals(MethodAccessLevel.PACKAGE,
		        ConfigUtils.getAccessLevel(
		                this.getClass().getDeclaredMethod("myPackageMethod")));
	}

	@Test
	public void testGetAccessLevelPrivate() throws Exception {
		Assert.assertEquals(MethodAccessLevel.PRIVATE,
		        ConfigUtils.getAccessLevel(
		                this.getClass().getDeclaredMethod("myPrivateMethod")));
	}

	@Test
	public void testGetAccessLevelProtected() throws Exception {
		Assert.assertEquals(MethodAccessLevel.PROTECTED,
		        ConfigUtils.getAccessLevel(this.getClass()
		                .getDeclaredMethod("myProtectedMethod")));
	}

	@Test
	public void testGetAccessLevelPublic() throws Exception {
		Assert.assertEquals(MethodAccessLevel.PUBLIC,
		        ConfigUtils.getAccessLevel(
		                this.getClass().getDeclaredMethod("myPublicMethod")));
	}

	@Test
	public void testGetMethod() {
		final Method method = this.myPrivateMethod();
		Assert.assertNotNull(method);
	}

	private Method myPrivateMethod() {
		// do nothing
		return null;
	}

	protected void myProtectedMethod() {
		// do nothing
	}

	void myPackageMethod() {
		// do nothing
	}
}

package org.beanone.xlogger.configure.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.beanone.xlogger.MethodAccessLevel;

public class ConfigUtils {
	private ConfigUtils() {
		// private for utility
	}

	public static MethodAccessLevel getAccessLevel(Method method) {
		final int modifier = method.getModifiers();
		if (Modifier.isPublic(modifier)) {
			return MethodAccessLevel.PUBLIC;
		} else if (Modifier.isProtected(modifier)) {
			return MethodAccessLevel.PROTECTED;
		} else if (Modifier.isPrivate(modifier)) {
			return MethodAccessLevel.PRIVATE;
		}

		return MethodAccessLevel.PACKAGE;
	}

	public static Method getMethod(JoinPoint point) {
		final MethodSignature signature = (MethodSignature) point
		        .getStaticPart().getSignature();
		return signature.getMethod();
	}
}

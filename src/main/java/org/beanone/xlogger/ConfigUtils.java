package org.beanone.xlogger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class ConfigUtils {
	private ConfigUtils() {
		// private for utility
	}

	/**
	 * Retrieves the access level from the passed in method.
	 *
	 * @param method
	 *            a {@link Method}.
	 * @return the access level of the method.
	 */
	public static MethodAccessLevel getAccessLevel(Method method) {
		if (method == null) {
			return null;
		}
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

	/**
	 * Retrieves the {@link Method} from the {@link JoinPoint}.
	 *
	 * @param point
	 *            a {@link JoinPoint}.
	 * @return the method where the JoinPoint is a pointcut.
	 */
	public static Method getMethod(JoinPoint point) {
		if (point == null) {
			return null;
		}
		final MethodSignature signature = (MethodSignature) point
		        .getStaticPart().getSignature();
		return signature.getMethod();
	}
}

package org.beanone.xlogger;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

/**
 * The key generator for method logging.
 *
 * @author Hongyan Li
 *
 */
@Component(ExceptionLoggingCacheKeyGenerator.NAME)
public class ExceptionLoggingCacheKeyGenerator implements KeyGenerator {
	public static final String NAME = "exceptionLoggingCacheKeyGenerator";

	@Override
	public Object generate(Object target, Method method, Object... params) {
		final AspectContext context = (AspectContext) params[0];
		return context.getException().getClass().getName();
	}
}

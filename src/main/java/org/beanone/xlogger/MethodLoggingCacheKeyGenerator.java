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
@Component(MethodLoggingCacheKeyGenerator.NAME)
public class MethodLoggingCacheKeyGenerator implements KeyGenerator {
	public static final String NAME = "methodLoggingCacheKeyGenerator";

	@Override
	public Object generate(Object target, Method method, Object... params) {
		final AspectContext context = (AspectContext) params[0];
		return context.getMethod().toString();
	}
}

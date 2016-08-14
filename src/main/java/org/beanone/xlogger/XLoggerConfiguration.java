package org.beanone.xlogger;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The spring configuration for XLogger.
 *
 * @author Hongyan Li
 *
 */
@Configuration
@EnableCaching
public class XLoggerConfiguration {
	public static final String METHOD_LOGGING_CACHE_ID = "org.beanone.xlogger.method";
	public static final String EXCEPTION_LOGGING_CACHE_ID = "org.beanone.xlogger.exception";

	/**
	 * @return the cache manager for xlogger.
	 */
	@Bean
	public CacheManager cacheManager() {
		final SimpleCacheManager cacheManager = new SimpleCacheManager();
		cacheManager.setCaches(
		        Arrays.asList(new ConcurrentMapCache(METHOD_LOGGING_CACHE_ID),
		                new ConcurrentMapCache(EXCEPTION_LOGGING_CACHE_ID)));
		return cacheManager;
	}
}

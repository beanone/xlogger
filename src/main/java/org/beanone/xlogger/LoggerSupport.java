package org.beanone.xlogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.slf4j.Logger;

/**
 * Utilities that supports logging.
 *
 * @author Hongyan Li
 *
 */
public class LoggerSupport {
	public static LoggerSpec DEFAULT_LOGGER_SPEC = new LoggerSpec() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return null;
		}

		@Override
		public LoggerLevel exceptionLevel() {
			return LoggerLevel.ERROR;
		}

		@Override
		public LoggerLevel level() {
			return LoggerLevel.TRACE;
		}

		@Override
		public String partition() {
			return "";
		}
	};

	private LoggerSupport() {
		// private for utility
	}

	public static void doLog(final String tag, final String methodName,
	        final LoggingHandler handler, final Logger logger,
	        final ArgumentSpecRegistry registry, final Throwable t,
	        final String[] names, final Object[] args) {
		handler.handle(logger, () -> {
			final StringBuilder builder = new StringBuilder();
			for (int i = 0; i < names.length; i++) {
				builder.append(names[i]);
				if (args[i] != null) {
					final ArgumentSpec<?> spec = registry
		                    .getSpec(args[i].getClass());
					builder.append('=').append(spec.asString(args[i]));
				}
				builder.append(", ");
			}
			if (builder.length() > 0) {
				builder.delete(builder.length() - 2, builder.length());
			}
			builder.insert(0, '(').insert(0, methodName).insert(0, ' ')
		            .insert(0, tag).append(')');
			return builder.toString();
		}, t);
	}

	/**
	 * Retrieves the {@link LoggerSpec} for the passed in method.
	 *
	 * @param method
	 *            the method whose {@link LoggerSpec} to be fetched.
	 * @return a {@link LoggerSpec}. Never null.
	 */
	public static LoggerSpec getLoggerSpec(Method method) {
		final LoggerSpec spec = method.getAnnotation(LoggerSpec.class);
		return spec == null ? DEFAULT_LOGGER_SPEC : spec;
	}
}

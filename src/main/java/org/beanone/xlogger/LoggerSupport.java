package org.beanone.xlogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Utilities that supports logging.
 *
 * @author Hongyan Li
 *
 */
public class LoggerSupport {
	/**
	 * The system default exception log level mapping.
	 */
	public static ExceptionSpec[] EXCEPTION_SPEC = new ExceptionSpec[0];
	/**
	 * The system default LoggerSpec.
	 */
	public static LoggerSpec DEFAULT_LOGGER_SPEC = new LoggerSpec() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return null;
		}

		@Override
		public ExceptionSpec[] exceptionLevel() {
			return EXCEPTION_SPEC;
		}

		@Override
		public LoggerLevel level() {
			return null;
		}

		@Override
		public String partition() {
			return "";
		}
	};

	private LoggerSupport() {
		// private for utility
	}

	/**
	 * Performs logging on behalf of the passed in logger.
	 *
	 * @param context
	 *            the logging context that all data needed to render a logging
	 *            message. Never null.
	 */
	public static void doLog(LoggingContext context) {
		context.getHandler().handle(context.getLogger(), () -> {
			final StringBuilder builder = new StringBuilder();
			final String[] names = context.getNames();
			final Object[] args = context.getArgs();
			for (int i = 0; i < names.length; i++) {
				builder.append(names[i]);
				if (args[i] != null) {
					final ArgumentSpec<?> spec = context.getRegistry()
		                    .getSpec(args[i].getClass());
					builder.append('=').append(spec.asString(args[i]));
				}
				builder.append(", ");
			}
			if (builder.length() > 0) {
				builder.delete(builder.length() - 2, builder.length());
			}
			builder.insert(0, '(').insert(0, context.getMethodName())
		            .insert(0, ' ').insert(0, context.getTag()).append(')');
			return builder.toString();
		}, context.getException());
	}

	/**
	 * Fetches the logging level for a given type of exception.
	 *
	 * @param specs
	 *            the {@link ExceptionSpec}s that contains the exception to log
	 *            level mapping. Never null.
	 * @param t
	 *            the Exception whose log level is resolved. May be null.
	 * @param defaultLevel
	 *            the default log level if a mapping for the passed in exception
	 *            type is not found.
	 * @return a log level. Never null.
	 */
	public static LoggerLevel getExceptionLevel(ExceptionSpec[] specs,
	        Throwable t, LoggerLevel defaultLevel) {
		if (t == null) {
			return defaultLevel;
		}
		for (final ExceptionSpec spec : specs) {
			if (t.getClass().equals(spec.exception())) {
				return spec.level();
			}
		}
		return defaultLevel;
	}

	/**
	 * Retrieves the {@link LoggerSpec} for the passed in method.
	 *
	 * @param method
	 *            the method whose {@link LoggerSpec} to be fetched. Never null.
	 * @return a {@link LoggerSpec}. Never null.
	 */
	public static LoggerSpec getLoggerSpec(Method method) {
		final LoggerSpec spec = method.getAnnotation(LoggerSpec.class);
		return spec == null ? DEFAULT_LOGGER_SPEC : spec;
	}
}

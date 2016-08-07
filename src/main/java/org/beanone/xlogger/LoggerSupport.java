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
	public static ExceptionSpec[] EXCEPTION_SPEC = new ExceptionSpec[0];
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

	/**
	 * Performs logging on behalf of the passed in logger.
	 *
	 * @param tag
	 *            a String that tags the logging.
	 * @param methodName
	 *            the name of the method logged.
	 * @param handler
	 *            the {@link LoggingHandler} to handle the logging.
	 * @param logger
	 *            the Logger that actually does the logging.
	 * @param registry
	 *            the registry that registers the {@link ArgumentSpec}s.
	 * @param t
	 *            the Exception to use in the logging.
	 * @param names
	 *            the names of the method arguments.
	 * @param args
	 *            the arguments.
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

	public static LoggerLevel getExceptionLevel(ExceptionSpec[] specs,
	        Throwable t) {
		if (t == null) {
			return LoggerLevel.ERROR;
		}
		for (final ExceptionSpec spec : specs) {
			if (t.getClass().equals(spec.exception())) {
				return spec.level();
			}
		}
		return LoggerLevel.ERROR;
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

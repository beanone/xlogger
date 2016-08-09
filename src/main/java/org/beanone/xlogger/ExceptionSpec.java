package org.beanone.xlogger;

/**
 * A specification for exception logging.
 *
 * @author Hongyan Li
 *
 */
public @interface ExceptionSpec {
	Class<? extends Throwable> exception();

	LoggerLevel level() default LoggerLevel.ERROR;
}

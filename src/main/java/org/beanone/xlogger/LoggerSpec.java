package org.beanone.xlogger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For one to define method logging specification.
 *
 * @author Hongyan Li
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoggerSpec {
	/**
	 * @return exception level mapping.
	 */
	ExceptionSpec[] exceptionLevel() default {};

	/**
	 * @return the {@link LoggerLevel} to use for the method annotated.
	 */
	LoggerLevel level() default LoggerLevel.TRACE;

	/**
	 * @return the partition to use for the method arguments.
	 */
	String partition() default "";
}

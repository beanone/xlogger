package org.beanone.xlogger;

import org.slf4j.Logger;

public class LoggingContext {
	private Object[] args;
	private Throwable exception;
	private LoggingHandler handler;
	private Logger logger;
	private String methodName;
	private String[] names;
	private ArgumentSpecRegistry registry;
	private String tag;

	public LoggingContext args(Object[] args) {
		this.args = args;
		return this;
	}

	public LoggingContext exception(Throwable exception) {
		this.exception = exception;
		return this;
	}

	public Object[] getArgs() {
		return this.args;
	}

	public Throwable getException() {
		return this.exception;
	}

	public LoggingHandler getHandler() {
		return this.handler;
	}

	public Logger getLogger() {
		return this.logger;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public String[] getNames() {
		return this.names;
	}

	public ArgumentSpecRegistry getRegistry() {
		return this.registry;
	}

	public String getTag() {
		return this.tag;
	}

	public LoggingContext handler(LoggingHandler handler) {
		this.handler = handler;
		return this;
	}

	public LoggingContext logger(Logger logger) {
		this.logger = logger;
		return this;
	}

	public LoggingContext methodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

	public LoggingContext names(String[] names) {
		this.names = names;
		return this;
	}

	public LoggingContext registry(ArgumentSpecRegistry registry) {
		this.registry = registry;
		return this;
	}

	public LoggingContext tag(String tag) {
		this.tag = tag;
		return this;
	}
}

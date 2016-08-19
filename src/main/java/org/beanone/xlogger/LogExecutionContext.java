package org.beanone.xlogger;

public class LogExecutionContext {
	private static final ThreadLocal<LogExecutionContext> CURRENT = new ThreadLocal<LogExecutionContext>() {
		@Override
		public LogExecutionContext initialValue() {
			return new LogExecutionContext();

		}
	};

	private Throwable handlingException;
	private String profile;

	private LogExecutionContext() {
		// private for thread local singleton
	}

	public static LogExecutionContext current() {
		return CURRENT.get();
	}

	public boolean checkSetHandlingException(Throwable handlingException) {
		if (this.handlingException == handlingException) {
			return false;
		}

		this.handlingException = handlingException;
		return true;
	}

	public Throwable getHandlingException() {
		return this.handlingException;
	}

	public String getProfile() {
		return this.profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
}

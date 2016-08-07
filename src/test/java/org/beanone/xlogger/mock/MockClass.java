package org.beanone.xlogger.mock;

import org.beanone.xlogger.LoggerSpec;

public class MockClass {
	public void methodException() {
		throw new IllegalArgumentException();
	}

	@LoggerSpec
	public void methodWithAnotation() {
	}

	@LoggerSpec
	public Object methodWithArgWithAnotation(MockArg arg) {
		return methodWithArgWithoutAnotation(arg);
	}

	public Object methodWithArgWithoutAnotation(MockArg arg) {
		methodWithAnotation();
		methodWithoutAnotation();
		return new MockResult();
	}

	public void methodWithoutAnotation() {
	}
}

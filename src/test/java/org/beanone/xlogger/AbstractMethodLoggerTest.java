package org.beanone.xlogger;

import org.beanone.xlogger.mock.MockArg;
import org.beanone.xlogger.mock.MockClass;
import org.beanone.xlogger.mock.MockResult;
import org.beanone.xlogger.mock.aspect.MockMethodLogger;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class AbstractMethodLoggerTest {
	@Test
	public void test() {
		ArgumentSpecRegistry.current().register(MockArg.class,
		        arg -> arg.getId());
		ArgumentSpecRegistry.current().register(MockResult.class,
		        arg -> arg.getResult());
		final MockClass clazz = new MockClass();
		clazz.methodWithArgWithAnotation(new MockArg());
		final ArgumentCaptor<String> captor1 = ArgumentCaptor
		        .forClass(String.class);
		final ArgumentCaptor<Throwable> captor2 = ArgumentCaptor
		        .forClass(Throwable.class);
		Mockito.verify(MockMethodLogger.MOCK_LOGGER, Mockito.atLeast(8))
		        .trace(captor1.capture(), captor2.capture());
	}

	@Test
	public void testException() {
		final MockClass clazz = new MockClass();
		try {
			clazz.methodException();
		} catch (final IllegalArgumentException e) {
			final ArgumentCaptor<String> captor1 = ArgumentCaptor
			        .forClass(String.class);
			final ArgumentCaptor<Throwable> captor2 = ArgumentCaptor
			        .forClass(Throwable.class);
			Mockito.verify(MockMethodLogger.MOCK_LOGGER, Mockito.times(1))
			        .error(captor1.capture(), captor2.capture());
		}
	}
}

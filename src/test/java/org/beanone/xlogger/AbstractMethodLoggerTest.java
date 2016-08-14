package org.beanone.xlogger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.FieldSignature;
import org.beanone.xlogger.mock.MockArg;
import org.beanone.xlogger.mock.MockClass;
import org.beanone.xlogger.mock.MockResult;
import org.beanone.xlogger.mock.aspect.MockMethodLogger;
import org.junit.Assert;
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
		Mockito.verify(MockMethodLogger.MOCK_LOGGER, Mockito.atLeast(10))
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

	@Test
	public void testHandlePjpNotForMethod() throws Throwable {
		final AbstractMethodLogger logger = new AbstractMethodLogger() {
		};

		final ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
		final StaticPart sp = Mockito.mock(StaticPart.class);
		Mockito.when(sp.getSignature())
		        .thenReturn(Mockito.mock(FieldSignature.class));
		Mockito.when(pjp.getStaticPart()).thenReturn(sp);
		Assert.assertNull(logger.handle(pjp, LoggerLevel.DEBUG));
	}

	@Test
	public void testHandleThrowsExceptionAlreadHandled() throws Throwable {
		final AbstractMethodLogger logger = new AbstractMethodLogger() {
		};

		final JoinPoint jp = Mockito.mock(JoinPoint.class);
		final StaticPart sp = Mockito.mock(StaticPart.class);
		Mockito.when(sp.getSignature())
		        .thenReturn(Mockito.mock(FieldSignature.class));
		Mockito.when(jp.getStaticPart()).thenReturn(sp);
		final Exception testException = new Exception();
		logger.handleThrows(jp, testException, new ExceptionSpec[0],
		        LoggerLevel.ERROR);
	}

	@Test
	public void testHandleThrowsPjpNotForMethod() throws Throwable {
		final AbstractMethodLogger logger = new AbstractMethodLogger() {
		};

		final JoinPoint jp = Mockito.mock(JoinPoint.class);
		final Exception testException = new Exception();
		LogExecutionContext.current().checkSetHandlingException(testException);
		logger.handleThrows(jp, testException, new ExceptionSpec[0],
		        LoggerLevel.ERROR);
	}
}

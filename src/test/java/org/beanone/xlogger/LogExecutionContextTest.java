package org.beanone.xlogger;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class LogExecutionContextTest {

	@Test
	public void testCheckSetHandlingException() {
		final Exception testException = new Exception();
		final Exception testException1 = new Exception();
		Assert.assertTrue(LogExecutionContext.current()
		        .checkSetHandlingException(testException));
		Assert.assertEquals(testException,
		        LogExecutionContext.current().getHandlingException());
		Assert.assertFalse(LogExecutionContext.current()
		        .checkSetHandlingException(testException));
		Assert.assertEquals(testException,
		        LogExecutionContext.current().getHandlingException());
		Assert.assertTrue(LogExecutionContext.current()
		        .checkSetHandlingException(testException1));
		Assert.assertEquals(testException1,
		        LogExecutionContext.current().getHandlingException());
	}

	@Test
	public void testGetSetPartition() {
		final String partition = "test" + UUID.randomUUID().toString();
		LogExecutionContext.current().setPartition(partition);
		Assert.assertEquals(partition,
		        LogExecutionContext.current().getPartition());
	}

}

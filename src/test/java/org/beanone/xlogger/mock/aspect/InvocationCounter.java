package org.beanone.xlogger.mock.aspect;

import java.util.concurrent.atomic.AtomicInteger;

import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.verification.VerificationMode;

public class InvocationCounter {

	private static class Counter
	        implements VerificationInOrderMode, VerificationMode {
		private final AtomicInteger count;

		private Counter(AtomicInteger count) {
			this.count = count;
		}

		@Override
		public VerificationMode description(String description) {
			return VerificationModeFactory.description(this, description);
		}

		public void verify(VerificationData data) {
			this.count.set(data.getAllInvocations().size());
		}

		public void verifyInOrder(VerificationDataInOrder data) {
			this.count.set(data.getAllInvocations().size());
		}

	}

	private InvocationCounter() {
	}

	public static <T> T countInvocations(T mock, AtomicInteger count) {
		return Mockito.verify(mock, new Counter(count));
	}

}

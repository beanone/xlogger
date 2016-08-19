package org.beanone.xlogger;

import java.util.ArrayList;
import java.util.List;

import org.beanone.xlogger.mock.MockArg;
import org.junit.Assert;
import org.junit.Test;

public class IterableArgumentSpecTest {

	@Test
	public void testDescribe() {
		final MockArg arg0 = createArgument("0");
		final MockArg arg1 = createArgument("1");
		final MockArg arg2 = createArgument("2");
		final List<MockArg> listArg = new ArrayList<>();
		listArg.add(arg1);
		listArg.add(arg2);
		arg0.setListArg(listArg);

		ArgumentSpecRegistry.current().register(Iterable.class,
		        new IterableArgumentSpec());

		final String part = "test";
		ArgumentSpecRegistry.initProfile(part);
		@SuppressWarnings("unchecked")
		final ArgumentSpec<MockArg> spec = new ConfiguredArgumentSpec(part,
		        "id, listArg");
		ArgumentSpecRegistry.current(part).register(MockArg.class, spec);
		Assert.assertEquals(
		        "{id=0, listArg=[{id=1, listArg=}, {id=2, listArg=}]}",
		        spec.asString(arg0));
	}

	private MockArg createArgument(String id) {
		final MockArg arg = new MockArg();
		arg.setId(id);
		return arg;
	}
}

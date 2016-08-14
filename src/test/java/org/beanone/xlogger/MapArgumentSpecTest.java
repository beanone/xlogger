package org.beanone.xlogger;

import java.util.HashMap;
import java.util.Map;

import org.beanone.xlogger.mock.MockArg;
import org.junit.Assert;
import org.junit.Test;

public class MapArgumentSpecTest {

	@SuppressWarnings({ "unchecked" })
	@Test
	public void testDescribe() {
		final MockArg arg0 = createArgument("0");
		final MockArg arg1 = createArgument("1");
		final MockArg arg11 = createArgument("11");
		final MockArg arg2 = createArgument("2");
		final MockArg arg21 = createArgument("21");
		final Map<MockArg, MockArg> mapArg = new HashMap<>();
		mapArg.put(arg1, arg11);
		mapArg.put(arg2, arg21);
		arg0.setMapArg(mapArg);

		ArgumentSpecRegistry.current().register(Map.class,
		        new MapArgumentSpec());

		final String part = "test";
		ArgumentSpecRegistry.initPartition(part);
		final ArgumentSpec<MockArg> spec = new ConfiguredArgumentSpec(part,
		        "id, mapArg");
		ArgumentSpecRegistry.current(part).register(MockArg.class, spec);
		Assert.assertEquals(
		        "{id=0, mapArg=[{id=1, mapArg=}={id=11, mapArg=}, {id=2, mapArg=}={id=21, mapArg=}]}",
		        spec.asString(arg0));
	}

	private MockArg createArgument(String id) {
		final MockArg arg = new MockArg();
		arg.setId(id);
		return arg;
	}
}

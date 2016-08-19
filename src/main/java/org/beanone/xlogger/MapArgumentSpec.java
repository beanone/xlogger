package org.beanone.xlogger;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class MapArgumentSpec implements ArgumentSpec<Map> {
	@SuppressWarnings("unchecked")
	@Override
	public String describe(Map arg) {
		final StringBuilder sb = new StringBuilder();
		final String profile = LogExecutionContext.current().getProfile();
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry
		        .current(profile);
		arg.forEach((k, v) -> {
			final ArgumentSpec<?> kSpec = registry.getSpec(k.getClass());
			final ArgumentSpec<?> vSpec = registry.getSpec(v.getClass());
			sb.append(kSpec.asString(k)).append('=').append(vSpec.asString(v))
		            .append(", ");
		});
		if (sb.length() > 0) {
			sb.delete(sb.length() - 2, sb.length());
		}
		sb.insert(0, '[').append(']');
		return sb.toString();
	}
}

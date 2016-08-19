package org.beanone.xlogger;

@SuppressWarnings("rawtypes")
public class IterableArgumentSpec implements ArgumentSpec<Iterable> {
	@SuppressWarnings("unchecked")
	@Override
	public String describe(Iterable arg) {
		final StringBuilder sb = new StringBuilder();
		final String profile = LogExecutionContext.current().getProfile();
		final ArgumentSpecRegistry registry = ArgumentSpecRegistry
		        .current(profile);
		arg.forEach(t -> {
			final ArgumentSpec<?> spec = registry.getSpec(t.getClass());
			sb.append(spec.asString(t)).append(", ");
		});
		if (sb.length() > 0) {
			sb.delete(sb.length() - 2, sb.length());
		}
		sb.insert(0, '[').append(']');
		return sb.toString();
	}
}

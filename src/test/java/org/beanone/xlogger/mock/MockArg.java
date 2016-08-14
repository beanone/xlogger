package org.beanone.xlogger.mock;

import java.util.List;
import java.util.Map;

public class MockArg {
	private String id = "1";
	private String attribute = "attribute";
	private List<MockArg> listArg;
	private Map<MockArg, MockArg> mapArg;

	@Override
	public boolean equals(Object other) {
		if (other instanceof MockArg) {
			final MockArg otherArg = (MockArg) other;
			return this.id.equals(otherArg.id);
		}

		return false;
	}

	public List<MockArg> getListArg() {
		return this.listArg;
	}

	public Map<MockArg, MockArg> getMapArg() {
		return this.mapArg;
	}

	public String getAttribute() {
		return this.attribute;
	}

	public String getId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	public void setListArg(List<MockArg> argList) {
		this.listArg = argList;
	}

	public void setMapArg(Map<MockArg, MockArg> argMap) {
		this.mapArg = argMap;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setId(String id) {
		this.id = id;
	}
}

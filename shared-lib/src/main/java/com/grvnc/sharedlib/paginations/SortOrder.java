package com.grvnc.sharedlib.paginations;

public enum SortOrder {

	ASC("ASC"), 
	DESC("DESC");
	private final String value;
	SortOrder(String v) {
		value = v;
	}

	public static SortOrder fromValue(String v) {
		for (SortOrder c : SortOrder.values()) {
			if (c.name().equals(v)) {
				return c;
			}
		}
		return ASC;
	}

	public String value() {
		return value;
	}

}

package com.grvnc.sharedlib.paginations;

import java.util.HashMap;
import java.util.Map;

public class SortBy {
	
	private Map<String, SortOrder> mapOfSorts;
	public SortBy() {
		if(null == mapOfSorts) {
			mapOfSorts = new HashMap<String, SortOrder>();
		}
	}
	public Map<String, SortOrder>  getSortBys() {
		return mapOfSorts;
	}
	
	public void addSort(String sortBy) {
		mapOfSorts.put(sortBy, SortOrder.ASC);
	}
	
	public void addSort(String sortBy, SortOrder sortOrder) {
		mapOfSorts.put(sortBy, sortOrder);
	}

}

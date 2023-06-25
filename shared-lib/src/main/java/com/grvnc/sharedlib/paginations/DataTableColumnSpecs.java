package com.grvnc.sharedlib.paginations;

public class DataTableColumnSpecs {

	private String name;
	private boolean searchable;
	private boolean orderable;
	private String search;
	private String sortDir;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public boolean isOrderable() {
		return orderable;
	}

	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSortDir() {
		return sortDir;
	}

	public void setSortDir(String sortDir) {
		this.sortDir = (null != sortDir) ? sortDir.toUpperCase() : sortDir;
	}

}

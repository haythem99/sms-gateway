package com.grvnc.sharedlib.paginations;

import java.util.List;

public class DataTableRequest {

	private Integer start;
	private Integer length;
	private String search;
	private List<DataTableColumnSpecs> columns;

	public DataTableRequest() {
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<DataTableColumnSpecs> getColumns() {
		return columns;
	}

	public void setColumns(List<DataTableColumnSpecs> columns) {
		this.columns = columns;
	}

}

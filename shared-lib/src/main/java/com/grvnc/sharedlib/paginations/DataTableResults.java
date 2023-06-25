package com.grvnc.sharedlib.paginations;

import java.util.HashMap;
import java.util.List;

public class DataTableResults {

	private long recordsFiltered;

	private long recordsTotal = 0;
//	@SerializedName("data")
	List listOfDataObjects;
	List<HashMap<String, String>> columnsMetadata;

//	public String getJson() {
//		return new Gson().toJson(this);
//	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public List getListOfDataObjects() {
		return listOfDataObjects;
	}

	public void setListOfDataObjects(List listOfDataObjects) {
		this.listOfDataObjects = listOfDataObjects;
	}

	public List<HashMap<String, String>> getColumnsMetadata() {
		return columnsMetadata;
	}

	public void setColumnsMetadata(List<HashMap<String, String>> columnsMetadata) {
		this.columnsMetadata = columnsMetadata;
	}

}

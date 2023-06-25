package com.grvnc.sharedlib.paginations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
public class DataTableUtil {
	private static boolean isCollectionEmpty(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static boolean isObjectEmpty(Object object) {
		if(object == null) return true;
		else if(object instanceof String) {
			if (((String)object).trim().length() == 0) {
				return true;
			}
		} else if(object instanceof Collection) {
			return isCollectionEmpty((Collection<?>)object);
		}
		return false;
	}
	
	public static String buildPaginatedQueryWithPagingSize(String baseQuery, PaginationCriteria paginationCriteria) {
		//String queryTemplate = "SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT BASEINFO.* FROM ( %s ) BASEINFO %s  %s ) FILTERED_ORDERD_RESULTS LIMIT %d, %d";
		StringBuilder sb = new StringBuilder("SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT BASEINFO.* FROM ( #BASE_QUERY# ) BASEINFO #WHERE_CLAUSE#  #ORDER_CLASUE# ) FILTERED_ORDERD_RESULTS LIMIT #PAGE_NUMBER#, #PAGE_SIZE#");
		String finalQuery = null;
		if(!DataTableUtil.isObjectEmpty(paginationCriteria)) {
			finalQuery = sb.toString().replaceAll("#BASE_QUERY#", baseQuery)
							.replaceAll("#WHERE_CLAUSE#", ((DataTableUtil.isObjectEmpty(paginationCriteria.getFilterByClause())) ? "" : " WHERE ") + paginationCriteria.getFilterByClause())
								.replaceAll("#ORDER_CLASUE#", paginationCriteria.getOrderByClause())
									.replaceAll("#PAGE_NUMBER#", paginationCriteria.getPageNumber().toString())
										.replaceAll("#PAGE_SIZE#", paginationCriteria.getPageSize().toString());
		}
		return (null == finalQuery) ?  baseQuery : finalQuery;
	}
	public static String buildPaginatedQueryWithoutPagingSize(String baseQuery, PaginationCriteria paginationCriteria) {
		//String queryTemplate = "SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT BASEINFO.* FROM ( %s ) BASEINFO %s  %s ) FILTERED_ORDERD_RESULTS LIMIT %d, %d";
		StringBuilder sb = new StringBuilder("SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT BASEINFO.* FROM ( #BASE_QUERY# ) BASEINFO #WHERE_CLAUSE# ) FILTERED_ORDERD_RESULTS ");
		String finalQuery = null;
		if(!DataTableUtil.isObjectEmpty(paginationCriteria)) {
			finalQuery = sb.toString().replaceAll("#BASE_QUERY#", baseQuery)
					.replaceAll("#WHERE_CLAUSE#", ((DataTableUtil.isObjectEmpty(paginationCriteria.getFilterByClause())) ? "" : " WHERE ") + paginationCriteria.getFilterByClause());
		}
		return (null == finalQuery) ?  baseQuery : finalQuery;
	}
//	public static List<String> getQueryColumns(String queryString, List<String> expectedColumns) {
//		List<String> columns = new ArrayList<String>();
//		queryString = queryString.replaceFirst("\\bSELECT\\b", "select").replaceFirst("\\bFROM\\b", "from");
//		queryString = queryString.replaceFirst("\\bselect\\b", "select ").replaceFirst("\\bfrom\\b", "from ");
//		String[] queryStringAsArray = queryString
//				.substring(queryString.indexOf("select ") + 6, queryString.indexOf("from ")).trim().split(",");
//		System.out.println(queryStringAsArray.length + "***********************************");
//		for (String column : queryStringAsArray) {
//			if (column.indexOf(" as ") != -1) {
//				column = column.split(" as ")[1];
//			} else {
//				throw new RuntimeException(" query columns not contain [as] word.issue in column[" + column + "] query["
//						+ queryString.substring(queryString.indexOf("select") + 6, queryString.indexOf("from")) + "]");
//			}
//			columns.add(column.trim());
//
//		}
////		if (null != expectedColumns) {
////			if(expectedColumns.size()!=columns.size()) {
////				throw new RuntimeException(" query [expectedColumns.size]!=[queryColumns.size]. expectedColumns["+Arrays.toString(expectedColumns.toArray()) +"] queryColumns["+Arrays.toString(columns.toArray())+"] ");
////			}
//			for (String expectedColumn : expectedColumns) {
//				boolean isExist = false;
//				l: for (String column : columns) {
//					if (expectedColumn.equals(column)) {
//						isExist = true;
//						break l;
//					}
//				}
//				if (!isExist) {
//					throw new RuntimeException(
//							" query [expectedColumns]list has one or more names that not included in [queryColumns]. expectedColumns["
//									+ Arrays.toString(expectedColumns.toArray()) + "] queryColumns["
//									+ Arrays.toString(columns.toArray()) + "] but expectedColumn[" + expectedColumn
//									+ "] not exist in query");
//				}
//			}
////		}
//		return columns;
//	}
//	public static List<String> getQueryColumnsOld(String queryString,List<String> expectedColumns){
//		List<String> columns=new ArrayList<String>();
////		String queryString = "SELECT  id, name as name, email as email FROM From selectTBL_USER";    
////		queryString=queryString.toLowerCase();
//		queryString=queryString.replaceFirst("\\bSELECT\\b", "select").replaceFirst("\\bFROM\\b", "from");
//		queryString=queryString.replaceFirst("\\bselect\\b", "select ").replaceFirst("\\bfrom\\b", "from ");
//		String[] queryStringAsArray = queryString.substring(queryString.indexOf("select ") +6, queryString.indexOf("from ")).trim().split(",");
//		System.out.println(queryStringAsArray.length+"***********************************");
//		for (String column : queryStringAsArray) {
//				if(column.indexOf(" as ")!=-1) {
//					column=column.split(" as ")[1];
//				}else {
//					throw new RuntimeException("datatable query columns not contain [as] word.issue in column["+column+"] query["+queryString.substring(queryString.indexOf("select") +6, queryString.indexOf("from"))+"]");
//				}
//				columns.add(column.trim());
//
//		}
////		String queryColumns="";
//		for (String expectedColumn : expectedColumns) {
//			boolean isExist=false;
//			l: for (String column : columns) {
//	//			queryColumns+="["+string+"]";
//	//			System.out.println(string);
//					if(expectedColumn.equals(column)) {
//						isExist=true;
//						break l;
//					}
//				}
//			if(!isExist) {
//				throw new RuntimeException("datatable query [expectedColumns]!=[queryColumns]. expectedColumns["+Arrays.toString(expectedColumns.toArray()) +"] queryColumns["+Arrays.toString(columns.toArray())+"] but expectedColumn["+expectedColumn+"] not exist in query");
//			}
//		}
////		if(!expectedColumns.equals(queryColumns)) {
////			throw new RuntimeException("datatable query [expectedColumns]!=[queryColumns]. expectedColumns["+expectedColumns +"] queryColumns["+queryColumns+"]");
////
////		}
//		return columns;
//	}
}

package com.grvnc.sharedlib.paginations;

import java.io.IOException;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UtilQureyService {
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private ResourceLoader resourceLoader;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	JdbcTemplate jdbc;

//	public List<HashMap<String, Object>> getQueryColumns(String queryPath,int numberOfInputsInQuery) throws RuntimeException, IOException {
//		String query = getQuery2(queryPath);
//		List<HashMap<String, Object>> columns = excuteQueryInternally(query, queryInputs).get("metaData");
//		return columns;
//	}

	public List<HashMap<String, Object>> excuteQueryByPath(String queryPath, List<String> expectedColumns,
			String... queryInputs) throws RuntimeException, IOException {
		String query = getQuerySQL(queryPath, queryInputs);
		return excuteQueryBySQL(query, expectedColumns);
	}

	public List<HashMap<String, Object>> excuteQueryBySQL(String query, List<String> expectedColumns)
			throws RuntimeException, IOException {

		List<String> dbColumnNames = getQueryColumnsBySQL(query).stream().map(e -> e.get("columnName"))
				.collect(Collectors.toList());
		Boolean valid = checkIfExpectedColumnsAreValid(dbColumnNames, expectedColumns);
		if (!valid) {
			throw new RuntimeException(
					" query [expectedColumns]list has one or more names that are not valid! queryColumns["
							+ dbColumnNames + "] but expectedColumn[" + expectedColumns + "] ");
		}
		List<HashMap<String, Object>> result = excuteQueryInternally(query, expectedColumns);

		if (null != expectedColumns && expectedColumns.size() > 0) {
			// remove unwanted columns from final result
			dbColumnNames.removeAll(expectedColumns);
			for (HashMap<String, Object> hash : result) {
				for (String columnName : dbColumnNames) {
					hash.remove(columnName);
				}
			}
		}
		return result;
	}

//	public List<HashMap<String, String>> getQueryColumnsByPath(String queryPath ) throws RuntimeException, IOException {
//			String query = getQuerySQL(queryPath);
//			return getQueryColumnsBySQL(query);
//		}
	public List<HashMap<String, String>> getQueryColumnsBySQL(String query) throws RuntimeException, IOException {
		List<HashMap<String, String>> metaDataList = new ArrayList<HashMap<String, String>>();
		jdbc.query(query, new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				try {
					int n = 0;
					ParameterMetaData metadata = preparedStatement.getParameterMetaData();
					int parameterCount = metadata.getParameterCount();
					for (int i = 1; i <= parameterCount; i++) {
						int dummyValue = 1;
						preparedStatement.setObject(++n, dummyValue);
					}
					ResultSetMetaData das = preparedStatement.getMetaData();
					for (int i = 1; i <= das.getColumnCount(); i++) {
						HashMap<String, String> metaData = new HashMap<String, String>();
						metaData.put("columnName", das.getColumnLabel(i));
						metaData.put("columnType", getDataType(das.getColumnTypeName(i)));
						metaData.put("isEnum", isEnum(das.getColumnTypeName(i)) + "");
						metaDataList.add(metaData);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
			}
		}, new RowMapper<HashMap<String, Object>>() {
			public HashMap<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				return null;
			}

		});
		return metaDataList;
	}

	public String getQuerySQL(String queryLocation, Object... params) throws RuntimeException, IOException {
		Resource fileResource = resourceLoader.getResource("classpath:" + queryLocation);
		String query = IOUtils.toString(fileResource.getInputStream(), "UTF-8");
		if (StringUtils.countOccurrencesOf(query, "?") != params.length) {
			throw new RuntimeException("can't get getQuery due query has lingth of [?]["
					+ StringUtils.countOccurrencesOf(query, "?") + "] not match input params[" + params.length + "] ");
		}
		return query = String.format(query.replaceAll("\\?", "%s"), params);
	}

	private Boolean checkIfExpectedColumnsAreValid(List<String> dbColumnNamesInput, List<String> expectedColumnsInput) {
		if (null == expectedColumnsInput || expectedColumnsInput.size() == 0)
			return true;

		ArrayList<String> dbColumnNames = new ArrayList<>(dbColumnNamesInput);
		ArrayList<String> expectedColumns = new ArrayList<>(expectedColumnsInput);
		dbColumnNames.removeAll(expectedColumns);
		if (dbColumnNamesInput.size() - dbColumnNames.size() == expectedColumns.size())
			return true;
		return false;

	}

	private List<HashMap<String, Object>> excuteQueryInternally(String query, List<String> expectedColumns)
			throws RuntimeException, IOException {

		List<HashMap<String, Object>> data = jdbc.query(query, new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) {
//	    	   		try {
//	        	   if(null!=queryInputs) {
//		        	   int n=0;
//		    			for (String queryInput : queryInputs) {
//		    				preparedStatement.setObject(++n, queryInput);
//		    			}
//	    	   		}
//	    	   		}catch (SQLException e) {
//						e.printStackTrace();
//						throw new RuntimeException(e.getMessage());
//					}
			}
		}, new RowMapper<HashMap<String, Object>>() {
			public HashMap<String, Object> mapRow(ResultSet rs, int rowNum) {
				HashMap<String, Object> row = new HashMap<String, Object>();
				try {
					ResultSetMetaData rsmd = rs.getMetaData();
					if (null == expectedColumns || expectedColumns.size() == 0) {
						int columnIndex = 1;
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							row.put(rsmd.getColumnLabel(columnIndex), rs.getObject(columnIndex));
							columnIndex++;
						}
					} else {
						for (String expectedColumn : expectedColumns) {
							row.put(expectedColumn, rs.getObject(expectedColumn));
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				return row;
			}

		});

		return data;
	}
//	public String getQuerySQL(String queryLocation) throws  IOException {
//		Resource fileResource = resourceLoader.getResource("classpath:" + queryLocation);
//		String query = IOUtils.toString(fileResource.getInputStream(), "UTF-8");
////		 if( StringUtils.countOccurrencesOf(query,"?")!=params.length) {
////			 throw new RuntimeException("can't get getQuery due query has lingth of [?]["+StringUtils.countOccurrencesOf(query,"?")+"] not match input params["+params.length+"] ");
////		 }
////		  return query= String.format(query.replaceAll("\\?", "%s"),params); 
//		return query;
//	}

//	private List<String> getQueryColumns_(String queryString, List<String> expectedColumns) {
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
//		if (null != expectedColumns) {
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
//		}
//		return columns;
//	}
	private String getDataType(String attrType) {
		// TODO Auto-generated method stub
		attrType = attrType.toUpperCase();
		if (attrType.contains("TIME") || attrType.contains("DATE") || attrType.contains("YEAR")) {
			return "date";
		} else if (attrType.contains("CHAR") || attrType.contains("TEXT")) {
			return "string";
		} else if (attrType.contains("BIT") || attrType.contains("INT") || attrType.contains("FLOAT")
				|| attrType.contains("DOUBLE") || attrType.contains("DEC")) {
			return "number";
		} else if (attrType.contains("BOOL")) {
			return "boolean";
		} else {
			throw new RuntimeException("could not simplify the column type as the attrType is[" + attrType
					+ "] please register this type ");
		}

	}

	private static boolean isEnum(String attrName) {
		String[] subNames = attrName.split("_");
		for (String subName : subNames) {
			if (subName.toLowerCase().equals("$enum")) {
				return true;
			}
		}
		return false;
	}
//	private static int getPriority(String attrName) {
//		String[] subNames=attrName.split("_");
//		for (String subName : subNames) {
//			if(subName.toLowerCase().startsWith("$priority")) {
//				int priority=Integer.parseInt(subName.substring("$priority".length(), subName.length()-1));
//				return priority;
//			}
//		}
//		return 0;
//	}
//	private static String getCleanColumnName(String attrName) {
//		int indexOfParam=attrName.indexOf("_$");
//		return indexOfParam==-1?attrName:attrName.substring(0,attrName.indexOf("_$"));
//	}
//	public static void main(String[] args) {
//		System.out.println(getPriority("wee_$priority12$_$enum"));
//		System.out.println(isEnum("wee_$priority12_$enum_$order1$_iiu"));
//		System.out.println(getCleanColumnName("wee__yy_$priority12$_$enum$_ubg"));
//		System.out.println(getCleanColumnName("wee__yy"));
//	}
//	public static void main(String[] args) {
//		System.out.println("dddd?dddd".replaceFirst("\\?", "1"));
//	}
}

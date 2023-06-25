package com.grvnc.sharedlib.paginations;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grvnc.sharedlib.ex.BusinessException;
import com.grvnc.sharedlib.ex.KeyValueExceptionMessage;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataTableService {

	/** The entity manager. */
//	@PersistenceContext
//	private EntityManager entityManager;

//	@Autowired  
//    JdbcTemplate jdbc;
	@Autowired
	private UtilQureyService qureyService;

//public String createDataTable2(DataTableRequest dataTableRequest,String baseQuery) throws RuntimeException, IOException {
//	
//	return createDataTable( dataTableRequest, baseQuery, );
//	
//}
	public DataTableResults createDataTableByPath(DataTableRequest dataTableRequest, String queryPath,
			String... queryInputs) throws RuntimeException, IOException {
		String baseQuery = qureyService.getQuerySQL(queryPath, queryInputs);
		DataTableResults dataTableResult = new DataTableResults();
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMapper.setDateFormat(df);
		if (null != dataTableRequest.getColumns() && dataTableRequest.getColumns().size() > 0
				&& dataTableRequest.getLength() > 0) {
			try {
				PaginationCriteria paginationCriteria = new PaginationCriteria(dataTableRequest);

				List<DataTableColumnSpecs> expectedColumnsSpecs = dataTableRequest.getColumns();

				List<String> expectedColumns = expectedColumnsSpecs.stream().map(e -> e.getName())
						.collect(Collectors.toList());

				String totalRecordsQuery = "select count(0) as total_number_of_records from(" + baseQuery
						+ ")base_query";
				long totalRecords = (Long) qureyService.excuteQueryBySQL(totalRecordsQuery, null).get(0)
						.get("total_number_of_records");
				String paginatedQuery = DataTableUtil.buildPaginatedQueryWithPagingSize(baseQuery, paginationCriteria);
				System.out.println(paginatedQuery);

				List<HashMap<String, Object>> paginatedQueryResult = qureyService.excuteQueryBySQL(paginatedQuery,
						expectedColumns);
				/////////////////////////////////////////////////////////////
				dataTableResult.setListOfDataObjects(paginatedQueryResult);
				if (!DataTableUtil.isObjectEmpty(paginatedQueryResult)) {
					dataTableResult.setRecordsTotal(totalRecords);

					if (paginationCriteria.isFilterByEmpty()) {
						dataTableResult.setRecordsFiltered(totalRecords);
					} else {

						String filteredPaginatedQuery = DataTableUtil.buildPaginatedQueryWithoutPagingSize(baseQuery,
								paginationCriteria);
						System.out.println(filteredPaginatedQuery);

						long filteredRowsSize = qureyService.excuteQueryBySQL(filteredPaginatedQuery, null).size();
						dataTableResult.setRecordsFiltered(filteredRowsSize);
					}
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
				throw new BusinessException(null, "query-execution-error", e.getMessage(),
						new KeyValueExceptionMessage("expectedColumns",
								objectMapper.writeValueAsString(qureyService.getQueryColumnsBySQL(baseQuery))));
			}
		}

		dataTableResult.setColumnsMetadata(qureyService.getQueryColumnsBySQL(baseQuery));
		log.debug(objectMapper.writeValueAsString(dataTableResult));
		return dataTableResult;
/////////////////////////////////////////////////////////////////
	}
//	private List<HashMap<String, Object>> excuteQueryInternally(String query ) throws RuntimeException, IOException {
//		List <HashMap<String, Object>> data = jdbc.query(query, new PreparedStatementSetter() {
//           public void setValues(PreparedStatement preparedStatement) throws SQLException {
//
//           
//           }
//        },
//           new RowMapper<HashMap<String, Object>>() {
//            public HashMap<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
//            	ResultSetMetaData rsmd = rs.getMetaData();
//            	HashMap<String, Object> row=new HashMap<String, Object>();
//            	int columnIndex=1;
//            	for (int i=1;i<=rsmd.getColumnCount();i++) {
//            		row.put(rsmd.getColumnLabel(columnIndex), rs.getObject(columnIndex));
//            		columnIndex++;
//            	}
//            	return row;
//            }
//            
//        });

//	return data;
//}

//	public List<HashMap<String, String>> getQueryColumns(String query ) throws RuntimeException, IOException {
//		List<HashMap<String, String>> metaDataList=new ArrayList<HashMap<String,String>>();
//		jdbc.query(
//				query, new PreparedStatementSetter() {
//					public void setValues(PreparedStatement preparedStatement) throws SQLException {
//						int n=0;
//						ParameterMetaData metadata = preparedStatement.getParameterMetaData();
//						int parameterCount = metadata.getParameterCount();
//						for (int i=1;i<=parameterCount;i++) {
//							preparedStatement.setObject(++n, 1);
//						}
//						ResultSetMetaData das=preparedStatement.getMetaData();
//						for (int i=1;i<=das.getColumnCount();i++) {
//							HashMap<String,String> metaData=new HashMap<String, String>();
//							metaData.put("columnName", das.getColumnLabel(i));
//							metaData.put("columnType", getDataType(das.getColumnTypeName(i)));
//							metaDataList.add(metaData);
//						}
//					}
//				},
//				new RowMapper<HashMap<String, Object>>() {
//					public HashMap<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
//						return null;
//					}
//					
//				});
//		
//		return metaDataList;
//	}
}

package com.grvnc.notification.integration.clientof.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grvnc.integrationlib.clientof.util.SystemConfigIVO;
 
 

@Service
public class UtilsServiceIntegrator {
 
	@Autowired
	private UtilClient utilClient;
 
  
	Logger Log = LoggerFactory.getLogger(UtilsServiceIntegrator.class);

 

 
 
@Cacheable(value="configValue",key="#configKey",unless="#result==null")
public  String getConfigValueOf(String configKey) throws Exception {
	String configValue="";
//	try {
		Log.debug("calling API getConfigValueOf and expected to get response  [String]");
		
		configValue= utilClient.getConfigValueOf(configKey); 
		
		Log.debug("API called and response is "+new ObjectMapper().writeValueAsString(configValue));
		
//	}catch (HttpStatusCodeException e) {
//		UtilsMS.handleHttpStatusCodeException(e);
//	} 
		return configValue;
}


 
public String addOrUpdateDynamicConfig(String key,String value ) throws JsonProcessingException {
	

	Log.debug("calling API addOrUpdateDynamicConfig and expected to get response  [String]");
	
	SystemConfigIVO configVO=new  SystemConfigIVO();
	configVO.setKey(key);
	configVO.setValue(value);

	String addingStatus=utilClient.addOrUpdateDynamicConfig(configVO); 
	
	Log.debug("API called and response is ["+addingStatus+"]");
	return addingStatus;
}
 


}

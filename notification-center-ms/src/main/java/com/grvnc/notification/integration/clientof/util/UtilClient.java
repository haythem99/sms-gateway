package com.grvnc.notification.integration.clientof.util;

 

import org.springframework.cloud.openfeign.FeignClient;

import com.grvnc.integrationlib.clientof.util.UtilClientImp;
import com.grvnc.integrationlib.conf.FeignClientConfiguration;
 
@FeignClient(
        contextId = "util-rest",
        name =  "http://grievance-utils-ms/",
        configuration = FeignClientConfiguration.class
)
public interface UtilClient extends UtilClientImp {
   
    
 
}
 
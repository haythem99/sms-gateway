package com.grvnc.integrationlib.conf;

 
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.RuntimeCryptoException;

import com.grvnc.integrationlib.ex.CarryOnInternalAPIException;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class FeignClientCustomErrorDecoder implements ErrorDecoder {
	 private ErrorDecoder errorDecoder = new Default();
	 
	 
	 
    @Override
    public Exception decode(String methodKey, Response response) {
    	
    	try {
        String requestUrl = response.request().url();
         Response.Body responseBody = response.body();
        String responseBodyAsString = IOUtils.toString(responseBody.asInputStream());;
        if(response.status()==409) {
        StringBuilder customeExceptionMessage = new StringBuilder();
		customeExceptionMessage.append("Exception happend durring FeignClient call!  [");
		customeExceptionMessage.append(response.status());
		customeExceptionMessage.append("] during [");
		customeExceptionMessage.append(response.request().httpMethod());
		customeExceptionMessage.append("] to [");
		customeExceptionMessage.append(requestUrl);
		customeExceptionMessage.append("] [");
		customeExceptionMessage.append(methodKey);
		customeExceptionMessage.append("]");
		String customeExceptionLogMessage_=customeExceptionMessage.toString();
        log.error(customeExceptionLogMessage_);
        return new CarryOnInternalAPIException(responseBodyAsString,customeExceptionLogMessage_,null);
        }else {
        	return errorDecoder.decode(methodKey, response);
        }
        }catch (Exception e) {
			// TODO: handle exception
        	return errorDecoder.decode(methodKey, response);
		} 
    }
    
 
}
package com.grvnc.integrationlib.ex;

 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grvnc.sharedlib.ex.ErrorResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
//@ResponseStatus(value = HttpStatus.CONFLICT)
@Slf4j
public class CarryOnInternalAPIException extends RuntimeException {
	private ErrorResponse httpResponseBody;
//	private int status=HttpStatus.INTERNAL_SERVER_ERROR.value();
	
	/**
	 * 
	 */
	public int getHTTPStatus() {
		return httpResponseBody.getStatus();
	}
	private static final long serialVersionUID = 1L;
	public CarryOnInternalAPIException() {
        super();
    }
 
    public CarryOnInternalAPIException(String httpResponseBody,String customExceptionLogMessage, Throwable cause ) throws JsonProcessingException {
    		super(customExceptionLogMessage!=null?customExceptionLogMessage:httpResponseBody, cause);
        try {
            ObjectMapper mapper = new ObjectMapper();
        	ErrorResponse httpResponseBodyObj = mapper.readValue(httpResponseBody, ErrorResponse.class);
        	httpResponseBodyObj.setErrorCode(httpResponseBodyObj.getErrorCode()==null?"unknown":httpResponseBodyObj.getErrorCode());
        	this.httpResponseBody=httpResponseBodyObj;	
        }catch (Exception e){
        	log.warn("can not convert httpResponseBody to json: {}",e.getMessage());
        	ErrorResponse errorResponse=new ErrorResponse();
        	errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        	errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
        	errorResponse.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        	errorResponse.setMessage(customExceptionLogMessage+" httpResponseBody:"+httpResponseBody);
        	this.httpResponseBody= errorResponse;
        }
    	
    }
    
    
 

    
}


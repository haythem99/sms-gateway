package com.grvnc.integrationlib.ex;

 

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "CarryOn_Internal_API_Exception_HANDLER")
//@RestControllerAdvice
public class CarryOnInternalAPIExceptionHandler    {
     
    @ExceptionHandler(CarryOnInternalAPIException.class)
    public ResponseEntity<Object> handleCarryOnInternalAPIException(CarryOnInternalAPIException ex ,WebRequest request ) {
    	log.error("CarryOnInternalAPIException: {}", ex.getMessage());
    	ex.printStackTrace();
    	int httpStatus=ex.getHTTPStatus();
    	return ResponseEntity.status(httpStatus).body(ex.getHttpResponseBody());
    }
     
}
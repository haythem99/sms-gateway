package com.grvnc.sharedlib.ex;

import java.util.Map.Entry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.grvnc.sharedlib.vovalidation.FormDataValidationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
//@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
		log.error("BusinessException: {}", ex.getMessage());
		ex.printStackTrace();
		int httpStatus = HttpStatus.CONFLICT.value();
		ErrorResponse errorResponse = new ErrorResponse(httpStatus, HttpStatus.CONFLICT.name(), ex.getMessage(),
				ex.getErrorCode(), request.getDescription(false), ex.getTimestamp());
		for (Entry<String, Object> error : ex.getErrors().entrySet()) {
			errorResponse.addValidationError(error.getKey(), error.getValue());
		}
		return ResponseEntity.status(httpStatus).body(errorResponse);
	}

	@ExceptionHandler(FormDataValidationException.class)
	public ResponseEntity<Object> handleBusinessException(FormDataValidationException ex, WebRequest request) {
		log.error("FormDataValidationException: {}", ex.getMessage());
		ex.printStackTrace();
		int httpStatus = HttpStatus.CONFLICT.value();
		ErrorResponse errorResponse = new ErrorResponse(httpStatus, HttpStatus.CONFLICT.name(), ex.getMessage(),
				ex.getErrorCode(), request.getDescription(false), ex.getTimestamp());
		for (Entry<String, Object> error : ex.getErrors().entrySet()) {
			errorResponse.addValidationError(error.getKey(), error.getValue());
		}
		return ResponseEntity.status(httpStatus).body(errorResponse);
	}
}
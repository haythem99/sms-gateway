package com.grvnc.sharedlib.ex;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.CONFLICT)
public class BusinessException extends RuntimeException {
	private Map<String, Object> errors = new HashMap<String, Object>();
	private String errorCode;
	private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause, String businessErrorCode, String message,
			KeyValueExceptionMessage... arg) {
		super(message, cause);
		errorCode = businessErrorCode;
		for (KeyValueExceptionMessage keyValueExceptionMessage : arg) {
			String key = keyValueExceptionMessage.getKey();
			String value = keyValueExceptionMessage.getValue();
			errors.put(key, value);
		}

	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}

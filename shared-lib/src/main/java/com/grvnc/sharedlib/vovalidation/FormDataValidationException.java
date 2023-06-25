package com.grvnc.sharedlib.vovalidation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.CONFLICT)
@Slf4j
public class FormDataValidationException extends RuntimeException {

	private Map<String, Object> errors = new HashMap<String, Object>();
	private String errorCode;
	private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FormDataValidationException() {
		super();
	}

	public FormDataValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public FormDataValidationException(Throwable cause, String message, List<FormDataValidationErrorVO> arg) {
		// super(message, cause);
		log.warn("data-validation-error", "user Entered not valid data message[" + message + "]");
		errorCode = "data-validation-error";
		for (FormDataValidationErrorVO keyValueExceptionMessage : arg) {
			String key = keyValueExceptionMessage.getAttrName();
			String value = keyValueExceptionMessage.getMessage();
			errors.put(key, value);
		}

	}

	public FormDataValidationException(String message) {
		super(message);
	}

	public FormDataValidationException(Throwable cause) {
		super(cause);
	}

}

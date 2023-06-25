package com.grvnc.sharedlib.ex;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor

public class ErrorResponse {
	private int status;
	private String error;
	private String message;
	private String errorCode;
	private String path;
	private String timestamp;
	private String stackTrace;
	private List<ValidationError> errors;

	public ErrorResponse(int status, String error, String message, String errorCode, String path, String timestamp) {
		this.status = status;
		this.error = error;
		this.message = message;
		this.errorCode = errorCode;
		this.path = path;
		this.timestamp = timestamp;
	}

	@Getter
	@Setter
	private static class ValidationError {
		private String key;
		private Object value;

		ValidationError(String key, Object value) {
			this.key = key;
			this.value = value;

		}
	}

	public void addValidationError(String key, Object value) {
		if (Objects.isNull(errors)) {
			errors = new ArrayList<>();
		}
		errors.add(new ValidationError(key, value));
	}
}
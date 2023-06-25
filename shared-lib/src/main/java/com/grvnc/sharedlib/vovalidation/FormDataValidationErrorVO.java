package com.grvnc.sharedlib.vovalidation;

public class FormDataValidationErrorVO {
	private String attrName;
	private String message;

	public FormDataValidationErrorVO() {
	}

	public FormDataValidationErrorVO(String title, String message) {
		this.message = message;

	}

	public FormDataValidationErrorVO(String message) {
		this.message = message;

	}

	public FormDataValidationErrorVO(String title, String message, String attrName) {
		this.message = message;
		this.attrName = attrName;

	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

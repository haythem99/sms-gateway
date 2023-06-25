package com.grvnc.notification.entites;

public enum SendingStatus {

	

	
	NEW(Constants.NEW_VALUE),
	SENT(Constants.SENT_VALUE),
	UNDER_PROCESS(Constants.UNDER_PROCESS_VALUE),
	TERMINATED(Constants.TERMINATED_VALUE),
	FAILED(Constants.FAILED_VALUE);

	SendingStatus(String stautsString) {
		if (!stautsString.equals(this.name())) {
			throw new IllegalArgumentException();
		}
	}

	    public static class Constants {
	        public static final String NEW_VALUE = "NEW";
	        public static final String SENT_VALUE = "SENT";
	        public static final String UNDER_PROCESS_VALUE = "UNDER_PROCESS";
	        public static final String TERMINATED_VALUE = "TERMINATED";
	        public static final String FAILED_VALUE = "FAILED";
	    }
}

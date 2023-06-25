package com.grvnc.sharedlib.viewobj;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO {
	private int resultCode;
	private String message;
	private Map<String, Object> data = new HashMap<>();

}

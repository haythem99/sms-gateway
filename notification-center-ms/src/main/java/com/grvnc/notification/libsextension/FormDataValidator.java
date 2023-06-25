package com.grvnc.notification.libsextension;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grvnc.sharedlib.vovalidation.UtilFormDataValidator;

@Configuration
public class FormDataValidator {

 
	@Bean 
	public UtilFormDataValidator  utilFormDataValidator(){
		return new UtilFormDataValidator();
	}
  
}
 
  
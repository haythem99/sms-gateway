package com.grvnc.sharedlib.vovalidation;

 
 
 
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.extern.slf4j.Slf4j;

 
@Slf4j
public class UtilFormDataValidator {
//	@Autowired
//	private static Validator validator;
	
	@Autowired
	private  Validator validator;
	
	public  List<FormDataValidationErrorVO> getErrorMessages(Object inputObject,String feilds[]) {
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Errors bindingResult = new BeanPropertyBindingResult( inputObject, inputObject.getClass().getName());
		validator.validate(inputObject, bindingResult);
		System.out.println("number of errors:"+bindingResult.getErrorCount());
		List<FormDataValidationErrorVO> result=new ArrayList<FormDataValidationErrorVO>();
		
		System.out.println("log all errors ");
		for (FieldError error : bindingResult.getFieldErrors()) {
			System.out.println("ObjectName["+error.getObjectName()+"] FieldName["+error.getField()+"] error["+error.getDefaultMessage()+"]");
		}
		System.out.println("log all errors done");
		
		for (String inputFieldName : feilds) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				String fieldName = fieldError.getField().replaceAll("\\[.*?\\]", "\\[\\]");;
			if(fieldName.equals(inputFieldName)) {
//				FieldError fieldError=bindingResult.getFieldError(fieldName);
				FormDataValidationErrorVO ce=new  FormDataValidationErrorVO();
				ce.setAttrName(fieldName);
				if(fieldError.isBindingFailure()) {
					ce.setMessage("enter.valid.data");
				}else {
					String message=fieldError.getDefaultMessage();
					ce.setMessage(message);
				}
				
				if(inputFieldName.indexOf("[]")!=-1) {
					ce.setMessage(fieldError.getField()+":"+ce.getMessage());
				}
				result.add(ce);
				break;
			}
			}
			
		}
		try {
			System.out.println("errors after checking pure fiels {}"+ow.writeValueAsString(result));
		}catch (Exception e) {
			System.out.println("errors after checking pure fiels {}"+result.size());
			// TODO: handle exception
		}
		if (bindingResult.hasGlobalErrors()) {
		for (String fieldName : feilds) {
			if (fieldName.indexOf("${") == 0 && fieldName.indexOf("}") == fieldName.length() - 1) {
				// validationName
				 fieldName = fieldName.substring(2, fieldName.length() - 1);
					for (ObjectError er : bindingResult.getGlobalErrors()) {
						String globalErrorsMessage = er.getDefaultMessage();
//						System.out.println("er.getDefaultMessage"+er.getDefaultMessage());
						for (String code : er.getCodes()) {
//							System.out.println("code"+code);
						}
						String objectName = "";
						if(fieldName.indexOf(".")!=-1) {
							objectName=fieldName.substring(0,fieldName.lastIndexOf(".") )+".";
						}
						if (globalErrorsMessage.indexOf(":") != -1) {
										String[] messageAsArray = globalErrorsMessage.split(":");
										String feildFullPath = objectName+messageAsArray[0];
										String message = messageAsArray[1];
										if(feildFullPath.equals(fieldName)) {
											FormDataValidationErrorVO ce = new FormDataValidationErrorVO(null, message, feildFullPath);
											result.add(ce);
										}
						}
					}
				}
			}
		}
		try {
			System.out.println("errors after checking globale $ fields {}"+ow.writeValueAsString(result));
	}catch (Exception e) {
		System.out.println("errors after checking globale $ fields {}"+result.size());
		// TODO: handle exception
	}
		for (FormDataValidationErrorVO formDataValidationErrorVO : result) {
			log.warn("errors based on selected fields "+formDataValidationErrorVO.getAttrName()+"//"+formDataValidationErrorVO.getMessage());
		}
		return result;
	}
//	public static void main(String[] args) {
//		String validationName = "a[aaaaa]a.bb.c[]c[33].d";
//		String validationName2 = validationName.replaceAll("\\[.*?\\]", "\\[\\]");
////		String validationName0=validationName.substring(validationName.indexOf(".") + 1,validationName.length() )+".";
////		String validationName2=validationName.substring(0,validationName.lastIndexOf(".") )+".";
//		System.out.println(validationName);
//		System.out.println(validationName2);
//	}
}

package com.grvnc.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.grvnc.integrationlib.clientof.notification.SMSIVO;
import com.grvnc.notification.service.NotificationService;
import com.grvnc.sharedlib.viewobj.ResponseVO;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")

@RequestMapping("/api/v3/internal/sending-sms")	
@Controller
@PreAuthorize("hasAnyRole('SYSTEM')")
@Slf4j
public class SendingSMSController {


 
 @Autowired
 NotificationService notificationService;
 	
 
	   @RequestMapping(value="/send", method=RequestMethod.POST )
	   @ResponseBody
	   public ResponseVO sendSMS(
			   @RequestBody SMSIVO smsIVO
			   ) throws Exception {
		   notificationService.initSMS(smsIVO);
			return new ResponseVO(200,"your messages have been accepted by Notification System",null);
	   }
	  
}

package com.grvnc.integrationlib.clientof.notification;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.grvnc.sharedlib.viewobj.ResponseVO;

public interface NotificationClientImp {

	@PostMapping("/notification-center/api/v3/internal/sending-sms/send")
	public ResponseVO sendSMS(@RequestBody SMSIVO smsVO);

//    @PostMapping("/notification-center/api/v3/internal/sms-conf/get-config-value-of")
//    public String getConfigValueOf(@RequestBody String key);

}
package com.grvnc.notification.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grvnc.notification.entites.NotificationConfigEntity;
import com.grvnc.notification.repositories.NotificationConfigRepository;

import lombok.extern.slf4j.Slf4j;

 
 

@Service
@Slf4j
public class NotificationConfigService {
 
	
	@Autowired
	private  NotificationConfigRepository notificationConfigRepository;
	public  String getConfigValueOf(String key) {
    	NotificationConfigEntity config=notificationConfigRepository.findByKey(key);
 		if(config!=null) {
			return config.getValue();
		}
		return null;
	}
      


 

}

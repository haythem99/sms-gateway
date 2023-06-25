package com.grvnc.notification.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grvnc.notification.entites.SMSEntity;
import com.grvnc.notification.entites.SendingStatus;
import com.grvnc.notification.repositories.SMSRepo;
import com.grvnc.sharedlib.util.UtilShared;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
public class NotificationServiceScheduling {

	@Autowired
	SMSRepo smsRepo;
 
	@Autowired
	NotificationService notificationService;
 
	@Scheduled(fixedDelay = 1000*15)
	public void sendSMS() throws IOException {
		String workerId=UUID.randomUUID().toString();
		log.info("sendSMS- Scheduling started with workerId {}",workerId);
		int numberOFEffectedRows=smsRepo.blockSMSsForWorker(workerId);
//		smsRepo.flushAndRefresh(null);
		log.info("numberOFEffectedRows {}",numberOFEffectedRows);
		List<SMSEntity> SMSEntityList = smsRepo.getUnderProcessSMSs(workerId);
		log.info("numberOF UnderProcessSMSs {}",SMSEntityList.size());
		SMSEntityList.stream().parallel().forEach(smsEntity ->{
			ObjectMapper mapper = new ObjectMapper();
			try {
				if(UtilShared.getDiffBtweenDatesInMinuts(smsEntity.getSmsRequestDate(),LocalDateTime.now() )<=smsEntity.getKeepAliveInMinutes()) {
					String message=smsEntity.getTemplateEntity().getTemplate();
					if(null!=smsEntity.getTemplateArgsAsJson()) {
						List<String> map = mapper.readValue(smsEntity.getTemplateArgsAsJson(), List.class);
						message=UtilShared.buildString(message,map);
					}
					String responseBody=notificationService.sendSMS(smsEntity.getToMobilesNumber(), message);
					smsRepo.updateSMSStatus(smsEntity.getId(),SendingStatus.SENT,responseBody,message);
				}else {
					smsRepo.updateSMSStatus(smsEntity.getId(),SendingStatus.TERMINATED,"message not alive when the worker fetch it",null);
					
				}
			}catch (Exception e) {
				smsRepo.updateSMSStatus(smsEntity.getId(),SendingStatus.FAILED,e.getMessage(),null);
			}
		});
		log.info("sendSMS- Scheduling finished");
	}
}

package com.grvnc.notification.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grvnc.integrationlib.clientof.notification.SMSIVO;
import com.grvnc.notification.entites.SMSEntity;
import com.grvnc.notification.entites.SenderNameEntity;
import com.grvnc.notification.entites.SendingStatus;
import com.grvnc.notification.entites.TemplateEntity;
import com.grvnc.notification.repositories.SMSRepo;
import com.grvnc.notification.repositories.SenderNameRepo;
import com.grvnc.notification.repositories.TemplateRepo;
import com.grvnc.sharedlib.ex.BusinessException;
import com.grvnc.sharedlib.vovalidation.FormDataValidationErrorVO;
import com.grvnc.sharedlib.vovalidation.FormDataValidationException;
import com.grvnc.sharedlib.vovalidation.UtilFormDataValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

	@Autowired
	SMSRepo smsRepo;
	@Autowired
	TemplateRepo templateRepo;
	@Autowired
	SenderNameRepo senderNameRepo;
	@Autowired
	UtilFormDataValidator utilFormDataValidator;
	@Autowired
	NotificationConfigService notificationConfigService;

	public void initSMS(SMSIVO smsIVO) throws JsonProcessingException {
		String[] feildsTovalidate = new String[] { "senderNameAlias", "languageCode", "templateName",
				"toMobilesNumbersList", "toMobilesNumbersList[]", "keepAliveInMinutes"

		};
		List<FormDataValidationErrorVO> outputErrors = utilFormDataValidator.getErrorMessages(smsIVO, feildsTovalidate);
		if (null == templateRepo.findByTemplateNameAndLanguageCode(smsIVO.getTemplateName(),
				smsIVO.getLanguageCode())) {
			FormDataValidationErrorVO ce = new FormDataValidationErrorVO();
			ce.setAttrName("templateName");
			ce.setMessage("notValidTemplateNameOrNotAvailableForGivenLanguageCode");
			outputErrors.add(ce);
		}
		if (null == senderNameRepo.findBySenderNameAlias(smsIVO.getSenderNameAlias())) {
			FormDataValidationErrorVO ce = new FormDataValidationErrorVO();
			ce.setAttrName("senderNameAlias");
			ce.setMessage("notValidSenderNameAlias");
			outputErrors.add(ce);
		}

		if (outputErrors.size() != 0) {
			throw new FormDataValidationException(null, "Form Data Input Issue", outputErrors);
		}
		List<SMSEntity> smsEntityList = new ArrayList<SMSEntity>();
		ObjectMapper mapper = new ObjectMapper();
		TemplateEntity templateEntity = templateRepo.findByTemplateNameAndLanguageCode(smsIVO.getTemplateName(),smsIVO.getLanguageCode());
		SenderNameEntity senderNameEntity = senderNameRepo.findBySenderNameAlias(smsIVO.getSenderNameAlias());
		for (String mobileNumber : smsIVO.getToMobilesNumbersList()) {

			SMSEntity smsEntity = new SMSEntity();
			smsEntity.setSendingStatus(SendingStatus.NEW);
			smsEntity.setToMobilesNumber(mobileNumber);
			smsEntity.setKeepAliveInMinutes(smsIVO.getKeepAliveInMinutes());
			;
			smsEntity.setSmsRequestDate(smsIVO.getCreationDate());
			;
			smsEntity.setTemplateArgsAsJson(
					null != smsIVO.getTemplateArgs() ? mapper.writeValueAsString(smsIVO.getTemplateArgs()) : null);
			;
			smsEntity.setTemplateEntity(templateEntity);
			smsEntity.setSenderNameEntity(senderNameEntity);
			smsEntityList.add(smsEntity);
		}
		smsRepo.saveAll(smsEntityList);

	}

	public String sendSMS(String mobileNumber, String message) throws JSONException {
		String responseBody="";
		if (Boolean.parseBoolean(notificationConfigService.getConfigValueOf("sms.sending.isenabled"))) {
			log.info("sending message to mobileNumber {} message length {}", mobileNumber, message.length());
			String url = notificationConfigService.getConfigValueOf("sms.send.url");
			String userName = notificationConfigService.getConfigValueOf("userName");
			String userSender = notificationConfigService.getConfigValueOf("userSender");
			String apiKey = notificationConfigService.getConfigValueOf("apiKey");
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			JSONObject payload = new JSONObject();
			payload.put("userName", userName);
			payload.put("numbers", mobileNumber);
			payload.put("userSender", userSender);
			payload.put("apiKey", apiKey);
			payload.put("msg", message);
			HttpEntity<String> request = new HttpEntity<String>(payload.toString(), headers);
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			log.debug(response.getStatusCode() + "");
			 responseBody=response.getBody();
			log.debug(response.getBody());
			log.debug(response.getStatusCodeValue() + "");
			log.debug(response.getHeaders() + "");
			JSONObject responseAsObj = new JSONObject(response.getBody());
			if (response.getStatusCodeValue() == 200
					&& (responseAsObj.get("code").equals("M0000") || responseAsObj.get("code").equals("1"))) {
				log.info("message sent successfully");
			} else {
				log.error("can not send sms" + response.getBody());
				throw new BusinessException(null, "can_not_send_sms", response.getBody());
			}
		}else {
			responseBody="sending SMSs is disabled by configuration";
			log.info("sending sms is disabled");
		}
		return responseBody;
	}

//	public static void main(String[] args) throws JSONException {
//		NotificationService notificationService = new NotificationService();
//		notificationService.sendSMS("", "");
//	}
}

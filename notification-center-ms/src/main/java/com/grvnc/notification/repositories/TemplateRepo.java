package com.grvnc.notification.repositories;

import com.grvnc.notification.entites.TemplateEntity;
import com.grvnc.sharedlib.repo.CustomRepository;

public interface TemplateRepo extends CustomRepository<TemplateEntity, Long> {

 
	TemplateEntity getById(long id);
	
//	TemplateEntity findByTemplateName(String templateName);
	TemplateEntity findByTemplateNameAndLanguageCode(String templateName,String languageCode);
}

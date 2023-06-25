package com.grvnc.notification.repositories;

import com.grvnc.notification.entites.SenderNameEntity;
import com.grvnc.sharedlib.repo.CustomRepository;

public interface SenderNameRepo extends CustomRepository<SenderNameEntity, Long> {

 
	SenderNameEntity getById(long id);
	
	SenderNameEntity findBySenderNameAlias(String senderNameAlias);
}

package com.grvnc.notification.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.grvnc.notification.entites.NotificationConfigEntity;


@Repository
public interface NotificationConfigRepository extends CrudRepository<NotificationConfigEntity, Integer>{

	public  NotificationConfigEntity findByKey(String key);
	
	
}
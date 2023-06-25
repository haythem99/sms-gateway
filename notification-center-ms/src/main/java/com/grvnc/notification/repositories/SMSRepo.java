package com.grvnc.notification.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.grvnc.notification.entites.SMSEntity;
import com.grvnc.notification.entites.SendingStatus;
import com.grvnc.sharedlib.repo.CustomRepository;

public interface SMSRepo extends CustomRepository<SMSEntity, Long> {

 
	SMSEntity getById(long id);
	@Query( value = "select smsEntity"
			+ " from SMSEntity smsEntity "
			+ " inner join fetch smsEntity.templateEntity"
			+ " where"
			+ " smsEntity.workerId=?1 "
			)
	List<SMSEntity> getUnderProcessSMSs(String workerId);
	
	@Modifying
	@Transactional
	@Query(
		value =   " update SMSEntity smsEntity "
				+ " set "
				+ "	 smsEntity.sendingStatus='"+SendingStatus.Constants.UNDER_PROCESS_VALUE+"'"
				+ "  ,smsEntity.workerId=?1 "
				+ " where "
				+ " 	smsEntity.sendingStatus='"+SendingStatus.Constants.NEW_VALUE+"' "
				+ "  or "
				+ "		("
				+ "		 smsEntity.sendingStatus='"+SendingStatus.Constants.FAILED_VALUE+"'  "
				+ "      and "
				+ "		 smsEntity.numberOfRetries< (select sedrName.maxNumberOfRetries from SenderNameEntity sedrName where smsEntity.senderNameEntity.id=sedrName.id)"
				+ "		)"
			 )

	int blockSMSsForWorker(String workerId);
	
	
	@Modifying
	@Transactional
	@Query(
			value =   " update SMSEntity smsEntity "
					+ " set "
					+ "	 smsEntity.sendingStatus=?2"
					+ "  ,smsEntity.note=?3 "
					+ "  ,smsEntity.actualSentMessage=?4"
					+ "	 ,smsEntity.numberOfRetries=numberOfRetries+1 "
					+ " where smsEntity.id=?1"
			)
	int updateSMSStatus(long id,SendingStatus status, String note,String actualSentMessage);
}

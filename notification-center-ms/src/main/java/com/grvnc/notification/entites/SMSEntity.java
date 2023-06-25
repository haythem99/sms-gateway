package com.grvnc.notification.entites;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tbl_sms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SMSEntity {

	 
	@Id //set it as primary key
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",updatable=false,insertable=false, nullable = false)
	private long id;
	
	
 
	@Column(name = "record_creation_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime recordCreationDate;
	@Column(name = "record_last_update_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)")
	private LocalDateTime recordLastUpdateDate;
	
	@Column(name = "sms_request_date", nullable = false, updatable = false, insertable = true)
	private LocalDateTime smsRequestDate;
 
	
    @ManyToOne
    @JoinColumn(name = "sender_name_id")
	private SenderNameEntity senderNameEntity;
	
 
	
 
	@ManyToOne
	@JoinColumn(name = "template_id")
	private TemplateEntity templateEntity;
	
	@Column(name = "template_args", nullable = true, updatable = false, insertable = true)
	private String templateArgsAsJson;
	
	@Column(name = "actual_sent_message", nullable = true, updatable = false, insertable = true)
	private String actualSentMessage;
	
	@Column(name = "to_mobiles_number", nullable = false, updatable = false, insertable = true)
	private String toMobilesNumber;
	
	@Column(name = "keep_alive_in_minutes", nullable = false, updatable = false, insertable = true)
	private int keepAliveInMinutes;
	
	@Enumerated(EnumType.STRING)
	@Column(name="sending_status",updatable=true,insertable=true, nullable = false)
	private SendingStatus sendingStatus;
	
	@Column(name = "number_of_retries", nullable = false, updatable = false, insertable = true)
	private int numberOfRetries;

	@Column(name = "worker_id", nullable = true, updatable = false, insertable = true)
	private String workerId;
	@Column(name = "note", nullable = true, updatable = false, insertable = true)
	private String note;
}

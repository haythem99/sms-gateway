package com.grvnc.notification.entites;

 
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tbl_notification_config")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "key")
//@Proxy(lazy = false) //to avoid exception could not initialize proxy [] - no Session   whcich was accour when use findOne(key) //https://stackoverflow.com/questions/20988626/what-is-proxy-in-the-context-of-load-method-of-hibernate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationConfigEntity {

	
	@Id //set it as primary key
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable = false, updatable = false, insertable = true )
	private int id;
	
//	@Id //set it as primary key
	@Column(name="key_", nullable = false, updatable = false, insertable = true )
	private String key;
	
	@Column(name="value", nullable = false, updatable = true, insertable = true )
	private String value;
	
	@Column(name="comment", nullable = true, updatable = false, insertable = true )
	private String comment;
	
    
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)")
	private Date lastUpdateDate;
 

  
	
}

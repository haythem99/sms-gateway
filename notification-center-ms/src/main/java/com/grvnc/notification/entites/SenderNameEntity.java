package com.grvnc.notification.entites;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="tbl_sender_name")
@Data
public class SenderNameEntity {

	
	@Id //set it as primary key
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",updatable=false,insertable=false, nullable = false)
	private long id;
    @Column(name="sender_name",unique = true,updatable=false,insertable=false, nullable = false)
    private String senderName;
    @Column(name="sender_name_alias",unique = true,updatable=false,insertable=false, nullable = false)
    private String senderNameAlias;
    
	@Column(name = "max_number_of_retries", nullable = false, updatable = false, insertable = false)
	private int maxNumberOfRetries;
    @OneToMany(mappedBy = "templateEntity")
	private List<SMSEntity> smsEntityList=new ArrayList<SMSEntity>();
}

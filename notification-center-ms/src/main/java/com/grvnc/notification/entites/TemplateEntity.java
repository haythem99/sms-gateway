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
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tbl_template",uniqueConstraints=
@UniqueConstraint(columnNames={"language_code", "template_name"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateEntity {

	 
	@Id //set it as primary key
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",updatable=false,insertable=false, nullable = false)
	private long id;
	
	@Column(name = "language_code", nullable = false, updatable = false, insertable = true)
	private String languageCode;
	
	@Column(name = "template_name", nullable = false, updatable = false, insertable = true)
	private String templateName;
	@Column(name = "template", nullable = false, updatable = false, insertable = true)
	private String template;
	 
    @OneToMany(mappedBy = "senderNameEntity")
	private List<SMSEntity> smsEntityList=new ArrayList<SMSEntity>();
}

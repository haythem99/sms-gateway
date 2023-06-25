package com.grvnc.sharedlib.outh2;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;

import com.grvnc.sharedlib.ex.BusinessException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserVO {
//	@Setter(AccessLevel.NONE)
//	private String compositeId;
//	@Setter(AccessLevel.NONE)
	private long id;
	private UserType userType;
	private String mobileNumber;
	private List<String> roles = new ArrayList<>();

//	public void setId(UserType userType,long id) {
//			this.userType=userType;
//			this.compositeId=userType+"."+id;
//	}
 
}

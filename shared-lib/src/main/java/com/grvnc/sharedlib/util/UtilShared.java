package com.grvnc.sharedlib.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class UtilShared {
  
	 

	public static long getDiffBtweenDatesInMinuts(LocalDateTime olderDate, LocalDateTime newerDate) {
		if(olderDate==null||newerDate==null) {
			return 60*24*365*100; //random big number
		}else {
			long seconds = (newerDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()-olderDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())/1000;
			return seconds/60;
		}
	}
	

	public static String getRandomName() {
		return UUID.randomUUID().toString().replace("-", "").replace("_", "");
	}
	 
 
 
 	public  static int getNewRandomOTP() {
		Random random = new Random();
		int otp = 1000 + random.nextInt(9000);
	return 1111;
		
	 
}
 
	public static  Date convertStringToDate(String dateInString,String format) throws ParseException {
//		"yyyy/MM/dd"
		DateFormat formatter = new SimpleDateFormat(format );
		Date date = formatter.parse(dateInString);
		 System.out.println(date);
         System.out.println(formatter.format(date));
	   return date;
	}
 
	public static String  buildString(String stringWithQuestionMarks,Object... params) throws RuntimeException, IOException{
		 if( StringUtils.countOccurrencesOf(stringWithQuestionMarks,"{?}")!=params.length) {
			 throw new RuntimeException("can't get buildString due string has length of [{?}]["+StringUtils.countOccurrencesOf(stringWithQuestionMarks,"{?}")+"] not match input params["+params.length+"] ");
		 }
		  return stringWithQuestionMarks= String.format(stringWithQuestionMarks.replaceAll("\\{\\?\\}", "%s"),params); 
	}
	public static void main(String[] args) throws RuntimeException, IOException {
		System.out.println(UtilShared.buildString("haythem {?} {?", "good"));
	}
}

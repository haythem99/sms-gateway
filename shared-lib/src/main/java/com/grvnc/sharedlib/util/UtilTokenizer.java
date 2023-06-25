package com.grvnc.sharedlib.util;

 
 

 
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
 

public class UtilTokenizer {
	
private static Algorithm algorithm= Algorithm.HMAC256("secretHAYTHRKL:K;245".getBytes());
	
private static  Logger log = LoggerFactory.getLogger(UtilTokenizer.class);

	

	
	public static  String getDataFromToken(String token) {
			JWTVerifier verifier=JWT.require(algorithm).build();
			DecodedJWT decodedJWT=verifier.verify(token);
			String data=decodedJWT.getSubject();
		return data;
	}
	
	public static String generateTokenForInput(String inputData,long expiresAfterXMinutes){

		String clipToken=JWT.create()
				.withSubject(inputData)
				.withExpiresAt(new Date(System.currentTimeMillis()+(expiresAfterXMinutes*60*1000)))
				.withIssuer("internalserver")
				.sign(algorithm);
		log.trace("a token has been generated for id["+inputData+"]");
		return  clipToken;
	}
	
 
	
 
}

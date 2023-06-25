package com.grvnc.sharedlib.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.grvnc.sharedlib.ex.BusinessException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AESEncryptor {
//	 private static SecretKeySpec secretKey;
	    private static byte[] key;
	    private static final String ALGORITHM = "AES";

	    public static SecretKeySpec prepareSecreteKey(String secretKey_) {
	        MessageDigest sha = null;
	        try {
	            key = secretKey_.getBytes(StandardCharsets.UTF_8);
	            sha = MessageDigest.getInstance("SHA-512");
	            key = sha.digest(key);
	            key = Arrays.copyOf(key, 16);
	           return   new SecretKeySpec(key, ALGORITHM);
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    public static String encrypt(String strToEncrypt,String secretKey_) {
	        try {
	        	SecretKeySpec secretKey= prepareSecreteKey(secretKey_);
	            Cipher cipher = Cipher.getInstance(ALGORITHM);
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	        } catch (Exception e) {
	           log.warn("Error while encrypting: " + e.toString());
	           e.printStackTrace();
	           throw new BusinessException(e, "encryption_error", "Error while encrypt: " + e.toString());
	        }
	    }

	    public static String decrypt(String strToDecrypt,String secretKey_) {
	        try {
	        	SecretKeySpec secretKey=prepareSecreteKey(secretKey_);
	            Cipher cipher = Cipher.getInstance(ALGORITHM);
	            cipher.init(Cipher.DECRYPT_MODE, secretKey);
	            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	        } catch (Exception e) {
	        	log.error("Error while decrypting: " + e.toString());
	        	e.printStackTrace();
	        	throw new BusinessException(e, "decryption_error_not_valid_input", "Error while decrypting: " + e.toString());
	        }
	         
	    }

	    public static void main(String[] args) {

	        String originalString = "haytem";

	        String encryptedString = AESEncryptor.encrypt(originalString,"ss");
	        String decryptedString = AESEncryptor.decrypt(encryptedString,"sks");

	        System.out.println(originalString);
	        System.out.println(encryptedString);
	        System.out.println(decryptedString);
	    }
}

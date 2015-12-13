package com.example.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5����
 * @author zhaimeng
 *
 */
public class MD5Utils {
	public static String encode(String password){
		MessageDigest instance;
		try {
			instance = MessageDigest.getInstance("MD5");
			byte[] disest = instance.digest(password.getBytes());//���ַ������ܣ������ֽ�����
			StringBuffer sb = new StringBuffer();
			for(byte b : disest){
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				if(hexString.length() < 2){
					hexString = "0" + hexString;//�����1λ�Ļ�����0
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}//��ȡMD5�㷨����
		
		return "";
	}
}

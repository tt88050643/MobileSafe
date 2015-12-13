package com.example.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * @author zhaimeng
 *
 */
public class MD5Utils {
	public static String encode(String password){
		MessageDigest instance;
		try {
			instance = MessageDigest.getInstance("MD5");
			byte[] disest = instance.digest(password.getBytes());//对字符串加密，返回字节数组
			StringBuffer sb = new StringBuffer();
			for(byte b : disest){
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				if(hexString.length() < 2){
					hexString = "0" + hexString;//如果是1位的话，补0
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}//获取MD5算法对象
		
		return "";
	}
}

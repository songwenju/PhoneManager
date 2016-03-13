package com.wjustudio.phoneManager.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 */
public class MD5Utils {
	public static String decode(String text){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(text.getBytes());
			StringBuilder sb = new StringBuilder();
			
			for (byte b : result) {
				int num = b&0xff;//+1加盐
				String hex = Integer.toHexString(num);
				if (hex.length()==1) {
					sb.append("0"+hex);
				}else {
					sb.append(hex);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}

package com.example.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.R.integer;

/**
 * ��ȡ���Ĺ���
 * @author zhaimeng
 *
 */
public class StreamUtils {
	public static String readFromStream(InputStream in) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = in.read(buffer)) != -1){
			out.write(buffer, 0, len);
		}
		
		String result = out.toString();
		in.close();
		out.close();
		return result;
	}
}

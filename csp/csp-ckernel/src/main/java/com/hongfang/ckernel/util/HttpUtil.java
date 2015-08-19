package com.hongfang.ckernel.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：HttpUtil.java<br>
 * 描             述 ：HttpUtil 處理有關 Http 的類別<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/3/4<P>
 * @deprecated
 */
public class HttpUtil {
	
	private static final int READ_TIMEOUT = 120 * 000;
	
	private static final int CONNECT_TIMEOUT = 30 * 000;
	
	private static final String ENCODING = "UTF-8";
	
	public static Object post(String url, Object obj) {
		String userVoStr = JsonUtil.toJson(obj);
		return null;
	}
	
	/**
	 * 
	  * sendRequest 說明：<br>
	  * @param url
	  * @param method
	  * @param params
	  * @return a String of remote response
	  * @throws IOException
	  * @author user
	 */
	public static String sendRequest(String url, String method, Map<String, String> params)
		throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		sendRequest(url, method, params, bos, CONNECT_TIMEOUT, READ_TIMEOUT);
		return bos.toString(ENCODING);
	}
	
	public static void sendRequest(String url, String method, Map<String, String> params, 
			OutputStream callback, int connectTimeout, int readTimeout) throws 
			IOException {
		String paramStr = null;
		if (params != null) {
			paramStr = convertParams(params);
			if (method.equalsIgnoreCase("get")) {
				url = url + "?" + paramStr;
			}
		}
		
		URL u = new URL(url);
		HttpURLConnection huc = (HttpURLConnection)u.openConnection();
		huc.setRequestMethod(method);
		huc.setDoInput(true);
		huc.setDoOutput(true);
		//true:follow轉址後的網址
		//不處理轉址行為
		huc.setInstanceFollowRedirects(false);
		huc.setConnectTimeout(connectTimeout);
		huc.setReadTimeout(readTimeout);
		
		if (method.equalsIgnoreCase("post")) {
			huc.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded; charset=utf-8");
			huc.setRequestProperty("Content-Length", paramStr);
		}
		
		huc.connect();
		
		if (method.equalsIgnoreCase("post")) {
			OutputStreamWriter out = new OutputStreamWriter(huc.getOutputStream());
			out.write(paramStr, 0, paramStr.length());
			out.flush();
		}
		
		System.out.println("HTTP Util Response Code :" + huc.getResponseCode());
		
		InputStream inputStream = huc.getInputStream();
		byte[] buffer = new byte[1024];
		while(inputStream.read(buffer) != -1) {
			callback.write(buffer);
		}
		inputStream.close();
		huc.disconnect();
	}
	
	private static String convertParams(Map<String, String> params) {
		StringBuffer s = new StringBuffer();
		
		Set<String> keys = params.keySet();
		for (Iterator<String> i = keys.iterator(); i.hasNext();) {
			String key = (String)i.next();
			String value = (String)params.get(key);
			try {
				s.append(key).append("=").
				append(URLEncoder.encode(value, ENCODING)).append("&");
			} catch (UnsupportedEncodingException e) {
				System.err.println(e.getMessage());
			}
		}
		s.delete(s.length() - 1, s.length());
		return s.toString();
	}
}

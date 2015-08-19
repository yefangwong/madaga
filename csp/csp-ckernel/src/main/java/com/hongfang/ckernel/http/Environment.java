/**
 * Copyright (c) 2014 Cornelius.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Cornelius. ("Confidential Information").
 *
 * You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of license agreement you entered
 * into with Cornelius.
 */
package com.hongfang.ckernel.http;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：Environment.java<br>
 * 描 述 ：<br>
 * 公 司 ：Hongfang intelligent technology.<br>
 * <br>
 * 【 資 料 來 源】 ：<br>
 * 【 輸 出 報 表】 ：<br>
 * 【 異 動 紀 錄】 ：<br>
 * 
 * @author : Mark Wong <br>
 * @version : 1.0.0 2014/7/19
 *          <P>
 */
public class Environment {
	/**
	 * You can generate API keys from the Cornelius Service Platform Service
	 */
	public final String apiKey;

	/**
	 * CSP subdomain. Could be your sandbox or production
	 */
	public final String siteName;

	/**
	 * Timeout value, in milliseconds, to be used when trying to conect to the
	 * cornelius service platform api server. If the timeout expires before the
	 * connection can be established, a java.net.SocketTimeoutException is
	 * raised. A timeout of zero is interpreted as an infinite timeout.
	 */
	 public int connectTimeout =
	 Integer.getInteger("com.hongfang.ckernel.api.http.timeout.connect", 15000);

	/**
	 * Timeout value, in milliseconds, to be used when reading response from the
	 * csp api server. If the timeout expires before there is data available for
	 * read, a java.net.SocketTimeoutException is raised. A timeout of zero is
	 * interpreted as an infinite timeout.
	 */
	public int readTimeout = Integer.getInteger("com.hongfang.ckernel.api.http.timeout.read", 60000);

	public static String CHARSET = "UTF-8";

	public static String API_VERSION = "v1";

	public static String LIBRARY_VERSION = "1.0.0";

	private final String apiBaseUrl;

	private static Environment defaultEnv;

	public Environment(String siteName, String apiKey) {
		this.apiKey = apiKey;
		this.siteName = siteName;
		// String domainSuffix =
		// System.getProperty("com.hongfang.api.domain.suffix", "hongfang.com");
		String proto = "http";
		// String proto = System.getProperty("com.hongfang.api.protocol",
		// "https");
		// this.apiBaseUrl = proto + "://" + siteName + "." + domainSuffix +
		// "/api/" + API_VERSION;
		if (siteName.isEmpty())
			this.apiBaseUrl = "http://54.251.104.102:8080/api/" + API_VERSION;
		else
			this.apiBaseUrl = proto + "://" + siteName + "/api/" + API_VERSION;
	}

	public static void configure(String siteName, String apikey) {
		Environment.defaultEnv = new Environment(siteName, apikey);
	}

	public static Environment defaultConfig() {
		if (defaultEnv == null) {
			throw new RuntimeException("Not yet configured");
		}
		return defaultEnv;
	}

	public String apiBaseUrl() {
		return this.apiBaseUrl;
	}
}

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
package com.hongfang.csp.api.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：SecurityInterceptor.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/8/25<P>
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		//get API key from client API
		//@deprecated
		/*
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null) 
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		else {
			String encodedClientAPIKeyToken = authHeader.replace("Basic ", "");
			String clientAPIKeyToken = new String(Base64.decodeBase64(encodedClientAPIKeyToken));
			//decode this API key to get ??
			
			//match this (key and ??) in CSP security DB
			//if NG
			String registeredAPIKey = "6201eb4dccc956cc4fa3a78dca0c2888177ec52efd48f125df214f046eb43138";
			String registeredAPIKeyToken = registeredAPIKey + ":";
			if (!clientAPIKeyToken.equals(registeredAPIKeyToken))
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}*/
		return true;
	}

}

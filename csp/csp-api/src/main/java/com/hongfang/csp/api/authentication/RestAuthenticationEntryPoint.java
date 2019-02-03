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
package com.hongfang.csp.api.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：RestAuthenticationEntryPoint.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/8/9<P>
 */
@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/* (non-Javadoc)
	 * @see org.springframework.security.web.AuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException arg2) throws IOException, ServletException {
		//get API key from client API
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
		}
		//else pass
	}

}

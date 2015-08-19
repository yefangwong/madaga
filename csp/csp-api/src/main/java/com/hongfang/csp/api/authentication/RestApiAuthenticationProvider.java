package com.hongfang.csp.api.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：RestApiAuthenticationProvider.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/8/9<P>
 */
public class RestApiAuthenticationProvider implements AuthenticationProvider {

	private SimpleGrantedAuthority anonymousRole = new SimpleGrantedAuthority("ROLE_ANONYMOUS");

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String username = authentication.getName();
		Object password = authentication.getCredentials();
		try {
			/*
			String hashedPassword = ESAPI.encryptor().hash((String) password, username);
			User user = userService.findByUsername(username);

			if (user == null) {
				throw new BadCredentialsException("Username not found."); // TODO message
			}

			if (!hashedPassword.equals(user.getPassword())) {
				throw new BadCredentialsException("Wrong password."); // TODO message
			}
		
			Collection<? extends GrantedAuthority> authorities = user.getAuthorities();*/
			Collection<? extends GrantedAuthority> authorities = null;
			return new UsernamePasswordAuthenticationToken(username, password, authorities);

		}
		//catch (EncryptionException e) {
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}

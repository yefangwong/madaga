package com.hongfang.ckernel.models;

import java.io.IOException;

import org.junit.Ignore;

import com.hongfang.ckernel.Result;
import com.hongfang.ckernel.http.Environment;
import com.hongfang.ckernel.util.JsonUtil;

@Ignore
public class UserTest {
	public static void main(String[] args) throws IOException {
		String apiSite = "localhost:8080";
		Environment.configure(apiSite, "apikey");

		User user = new User();
		user.name = "mark";
		user.password = "password";
		Result result = user.login().request();
		
		//Result result = User.login(userVo.name, userVo.password).request();
		user = result.user();
		System.out.println(user.loginSuccess);

		System.out.println(JsonUtil.toJson(user));
	}
}

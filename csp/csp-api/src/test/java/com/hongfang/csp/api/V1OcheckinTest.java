package com.hongfang.csp.api;

import com.hongfang.ckernel.Result;
import com.hongfang.ckernel.http.Environment;
import com.hongfang.ckernel.models.LocationEnum;
import com.hongfang.ckernel.models.User;

public class V1OcheckinTest {
	private static final Logger logger = LoggerFactory.getLogger(V1OcheckinTest.class);

	public static void main(String[] args) {
		//String apiSite = "localhost:8080"; //DEV
		String apiSite = "54.251.104.102:8080"; //SIT

		try {
			Environment.configure(apiSite, "6201eb4dccc956cc4fa3a78dca0c2888177ec52efd48f125df214f046eb43138");
			
			// Case 1:Test user login
			User user = new User();
			user.name = "markwong";
			user.password = getEncryptedPassword();
			
			Result result = user.login().request();
			user = result.user();

			if (user.loginSuccess) {
				logger.info("LOGIN_SUCCESS");
			} else {
				logger.info("LOGIN_FAIL");
			}
			
			// Case 2:Test user create
			user.email="yefang.wong@gmail.com";
			user.birthMonth = "10";
			user.birthDay = "16";
			user.tel = "0981170518";
			user.fellowshipId = "1";
			result = user.create().request();
			user = result.user();
			logger.info("return user seq is " + user.seq);
			
			// Case 3:Test user checkin
			user.locationId = LocationEnum.FELLOWSHIP.toString();
			result  = user.checkin().request();
			user = result.user();
			logger.info("return user checkin datetime is " + user.checkinDateTime);
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
	
	private static String getEncryptedPassword() {
		return "1234567";
	}
}

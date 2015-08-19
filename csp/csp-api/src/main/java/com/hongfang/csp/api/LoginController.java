package com.hongfang.csp.api;

import org.occ.csp.domain.ChurchMember;
import org.occ.csp.service.CspService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongfang.ckernel.models.User;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：V1Controller.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/7/31<P>
 */
@Controller
@RequestMapping("/v1")
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private CspService cspService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody User login(@RequestBody User user) {
		logger.info("LoginController login");
		ChurchMember cm = cspService.getChurchMemberByEmail(user.email);
		if (cm != null) {
			String password = cm.getMemberPassword();
			user.loginSuccess = user.password.equals(password)?true:false;
			return user;
		} else {
			user.loginSuccess = false;
			return user;
		}
	}
}

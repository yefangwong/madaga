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
package com.hongfang.csp.api.user;

import java.util.Date;

import org.apache.http.client.utils.DateUtils;
//import org.occ.csp.domain.ChurchMember;
//import org.occ.csp.domain.Fellowship;
//import org.occ.csp.domain.Footprint;
//import org.occ.csp.service.CspService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongfang.ckernel.models.LocationEnum;
import com.hongfang.ckernel.models.User;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：UserController.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/8/29<P>
 */
@Controller
@RequestMapping("/v1/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
	//@Autowired
	//private CspService cspService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody User createUser(@RequestBody User user) throws Exception {
		logger.info("UserController createUser");
		/*ChurchMember cm = new ChurchMember();
		cm.setMemberAccount(user.email);
		cm.setMemberName(user.name);
		cm.setMemberPassword(user.password);
		cm.setMemberLoginDate(new Date());
		Fellowship fellowship = new Fellowship();
		fellowship.setFellowshipId(user.fellowshipId);
		cm.setResideIn(fellowship);
		
		cm.setCreateDate(new Date());
		cm.setCreateUid("mark");
		
		cspService.saveChurchMember(cm);*/
		user.seq = "1";
		return user;
	}
	
	@RequestMapping(value = "/checkin", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody User checkin(@RequestBody User user) {
		logger.info("UserController checkin");
		user.checkinDateTime = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		logger.info(user.name + " check from " + LocationEnum.getNameFromId(user.locationId) +
				" at " + user.checkinDateTime);
		/*Footprint fp = new Footprint();
		fp.setMemberSid(user.seq);
		fp.setLoginDate(new Date());
		fp.setCreateDate(new Date());
		fp.setCreateUid("mark");
		cspService.saveFootprint(fp);*/
		return user;
	}
}

package com.hongfang.ckernel.models;

import java.io.IOException;

import com.hongfang.ckernel.models.internel.HttpUtil.Method;
import com.hongfang.ckernel.models.internel.Request;
import com.hongfang.ckernel.models.internel.Resource;
import com.hongfang.ckernel.util.JsonUtil;

/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：User.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/7/19<P>
 */
public class User extends Resource<User>{
	public String seq;
	public String email;
	public String name;
	public String fellowshipId;
	public String password;
	public String birthMonth;
	public String birthDay;
	public String tel;
	public boolean loginSuccess;
	public String locationId;      // 打卡位置
	public String checkinDateTime; // 打卡時間
	
	public Request login() throws IOException {
		String uri = uri("login");
		return new LoginRequest(Method.POST, uri, this);
	}
	
	public Request create() throws IOException {
		String uri = uri("user/create");
		return new CreateRequest(Method.POST, uri, this);
	}

	public Request checkin() throws IOException {
		String uri = uri("user/checkin");
		return new CreateRequest(Method.POST, uri, this);
	}

	// Operation Request Classes
	//==========================
	public class LoginRequest extends Request<LoginRequest> { 
	
		private LoginRequest(Method httpMeth, String uri, User user) {
			super(httpMeth, uri, JsonUtil.toJson(user));
		}
	}
	
	public class CreateRequest extends Request<CreateRequest> {
		private CreateRequest(Method httpMeth, String uri, User user) {
			super(httpMeth, uri, JsonUtil.toJson(user));
		}
	}
	
	public class CheckinRequest extends Request<CheckinRequest> {
		private CheckinRequest(Method httpMeth, String uri, User user) {
			super(httpMeth, uri, JsonUtil.toJson(user));
		}
	}

}

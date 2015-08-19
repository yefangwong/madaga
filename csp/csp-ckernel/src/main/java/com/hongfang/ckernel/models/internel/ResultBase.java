package com.hongfang.ckernel.models.internel;

import com.hongfang.ckernel.models.User;
import com.hongfang.ckernel.util.JsonUtil;

public class ResultBase {
	
	private String json;
	
	public ResultBase(String json) {
		this.json = json;
	}
	
	public User user() {
		return (User)get("user");
	}
	
	@SuppressWarnings("rawtypes")
	private Resource get(String key) {
		@SuppressWarnings("unchecked")
		Class<Resource> modelClaz = ClazzUtil.getClaz(key);
		return JsonUtil.toObject(json, modelClaz);
	}
	
	 @Override
	 public String toString() {
		 return json;
	 }
}

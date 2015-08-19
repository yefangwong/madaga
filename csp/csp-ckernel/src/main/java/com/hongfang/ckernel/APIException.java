package com.hongfang.ckernel;

public class APIException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2586620702924215818L;
	private String jsonString;
	public APIException(String json) {
		this.jsonString = json;
	}
	
	@Override
	public String toString() {
		return jsonString;
	}
}

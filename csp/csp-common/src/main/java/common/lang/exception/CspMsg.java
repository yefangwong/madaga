package common.lang.exception;

import common.enums.CommonMsgCodeEnums;

public class CspMsg extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1122567224226206321L;
	
	private CommonMsgCodeEnums msgCodeEnums;
	
	public CspMsg(CommonMsgCodeEnums msgCodeEnums) {
		this.msgCodeEnums = msgCodeEnums;
	}

}

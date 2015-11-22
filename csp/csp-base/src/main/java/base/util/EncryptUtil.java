package base.util;

import common.lang.exception.CspMsg;

public class EncryptUtil {
	private final static String KEY = "JesusIsLORD";
	
	public static Object encrypt(Object value) {
		if ((value == null) || (value.toString().length() < 1)) {
			return value;
		}
		try {
			value = AESCoder.encrypt(value.toString(), KEY);
		} catch (CspMsg e) {
			return value;
		}
		return value;
	}

	public static Object decrypt(Object value) {
		if ((value == null) || (value.toString().length() < 1)) {
			return value;
		}
		try {
			value = AESCoder.decrypt(value.toString(), KEY);
		} catch (CspMsg e) {
			return value;
		}
		return value;
	}

}
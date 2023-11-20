/*
 * Copyright 2022 yefangwong(https://github.com/yefangwong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dehongfang.csp.base.util;

import common.lang.exception.CspMsg;

public class EncryptUtil {
	private final static String str = "JesusIsLORD";

	public static Object encrypt(Object value) {
		if ((value == null) || (value.toString().length() < 1)) {
			return value;
		}
		try {
			value = AESCoder.encrypt(value.toString(), str);
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
			value = AESCoder.decrypt(value.toString(), str);
		} catch (CspMsg e) {
			return value;
		}
		return value;
	}

}

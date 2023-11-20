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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import common.utils.AbstractCustomLogger;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：BaseLogger.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2015/10/03<P>
 */
public class BaseLogger extends AbstractCustomLogger {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6225815834564050L;

	public BaseLogger(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public String customLogFormat(String log) {
		 String methodName = "";
		 int lineNumber = -1;
		 try {
			 methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
			 if (methodName != null) {
				 if (methodName.length() > 30) {
					 methodName = methodName.substring(0, 30);
				 } else {
					 methodName = StringUtils.leftPad(methodName, 30);
				 }
			 }
			 methodName = methodName + "|";
		 
			 lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
		 } catch (Throwable e) {
			 getLog().warn(ExceptionUtils.getFullStackTrace(e));
		 }
		 
		 return String.format("%s[%s] %s", 
				 new Object[]{methodName, lineNumber, log});
	}
}

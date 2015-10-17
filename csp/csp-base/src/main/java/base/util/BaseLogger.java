package base.util;

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

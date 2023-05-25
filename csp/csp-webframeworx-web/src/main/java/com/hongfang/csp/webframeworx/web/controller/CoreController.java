package com.hongfang.csp.webframeworx.web.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/*Fix CVE-2020-5408(6.5), CVE-2022-22978(9.8), CVE-2021-22112(8.8), CVE-2020-5407(8.8),
		CVE-2022-22976(5.3), CVE-2021-22119(7.5)*/
//import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * AP Controller 請務必繼承此類別
 * 此類別提供 Session 存取，設計一個 scope for AP 的 Session 存放區
 * 平台會統一對此 Session 作 create 及清除
 * 
 * 平台也在這 handler exception，當系統層的 Exception 發生時，
 * 若 AP 層沒有處理，則會由平台統一處理。
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：CoreController.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/3/11<P>
 */
@Component
public class CoreController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 系統執行發生錯誤時，請工程師協助了解問題發生原因
	 */
	/*Fix CVE-2020-5408(6.5), CVE-2022-22978(9.8), CVE-2021-22112(8.8), CVE-2020-5407(8.8),
		CVE-2022-22976(5.3), CVE-2021-22119(7.5)*/
	//@ExceptionHandler(Exception.class)
	public String handleException(Exception e, HttpServletRequest request) {
		logger.error("系統執行過程發生錯誤，請工程師協助了解問題發生原因。");
		return "exception/exception";
	}

	/*Fix CVE-2020-5408(6.5), CVE-2022-22978(9.8), CVE-2021-22112(8.8), CVE-2020-5407(8.8),
		CVE-2022-22976(5.3), CVE-2021-22119(7.5)*/
	//@ExceptionHandler(Error.class)
	public String handleError(Exception e, HttpServletRequest request) {
		logger.error("系統執行過程發生錯誤，請工程師協助了解問題發生原因。");
		return "exception/exception";
	}

}

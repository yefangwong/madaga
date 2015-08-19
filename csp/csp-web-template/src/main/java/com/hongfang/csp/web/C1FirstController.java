package com.hongfang.csp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 1.寫出第一個 Spring MVC Controller
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：C1FirstController.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/7/31<P>
 */
@Controller
@RequestMapping("/c1")
public class C1FirstController {
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "c1/index";
	}
}

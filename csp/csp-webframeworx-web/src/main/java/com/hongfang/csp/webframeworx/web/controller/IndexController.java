package com.hongfang.csp.webframeworx.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：IndexController.java<br>
 * 描             述 ：首頁 Controller<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : yfwong <br>
 * @version  : 0.1 Jan 26, 2019<P>
 */
@Controller
public class IndexController {
	@RequestMapping("/")
	public String indexPage(ModelMap model) {
		return "/index";
	}
}

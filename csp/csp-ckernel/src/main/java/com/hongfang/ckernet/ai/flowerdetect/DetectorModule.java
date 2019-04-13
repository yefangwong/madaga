package com.hongfang.ckernet.ai.flowerdetect;

import com.google.inject.AbstractModule;

/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：DetectorModule.java<br>
 * 描             述 ：辨識模組，模組是一種 Google Guice 的實踐，定義辨識作業的介面和實作。<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : yfwong <br>
 * @version  : 0.1 Apr 13, 2019<P>
 */
public class DetectorModule extends AbstractModule {

	@Override
	protected void configure() {
	    bind(DetectorService.class).to(FlowerDetectorService.class);
	}

}

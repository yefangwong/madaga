package com.hongfang.ckernet.ai.flowerdetect;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：FlowerDetector.java<br>
 * 描             述 ：希望這支程式能夠告訴我們相片中的花的名子。<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : yfwong <br>
 * @version  : 0.1 Apr 13, 2019<P>
 */
public class FlowerDetector {
	public FlowerCategory detect(Flower flower) { 
		Injector injector = Guice.createInjector(new DetectorModule());
		DetectorService flowerDetectorService = injector.getInstance(FlowerDetectorService.class);
		return flowerDetectorService.detect(flower);
	}
}

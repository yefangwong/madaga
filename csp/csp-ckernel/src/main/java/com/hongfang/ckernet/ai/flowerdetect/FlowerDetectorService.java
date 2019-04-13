package com.hongfang.ckernet.ai.flowerdetect;

/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：FlowerDetectorService.java<br>
 * 描             述 ：一種花朵名稱的電腦軟體辨識器，希望將來有電腦硬體的辨識器，例如：手機、花朵偵測器、花朵偵測晶片等。<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : yfwong <br>
 * @version  : 0.1 Apr 13, 2019<P>
 */
public class FlowerDetectorService implements DetectorService {

	@Override
	public FlowerCategory detect(Entity flower) {
		//TODO detect Flower Entity name
		// return FlowerCategory
		return new FlowerCategory();
	}

}

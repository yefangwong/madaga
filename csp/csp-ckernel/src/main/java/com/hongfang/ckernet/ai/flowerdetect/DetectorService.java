package com.hongfang.ckernet.ai.flowerdetect;

/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：DetectorService.java<br>
 * 描             述 ：電腦偵測物體的服務介面，希望將來這介面能提供更多服務幫助人們辨識未知（或已知）物體。<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : yfwong <br>
 * @version  : 0.1 Apr 13, 2019<P>
 */
public interface DetectorService {

	FlowerCategory detect(Entity entity);

}

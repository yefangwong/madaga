package com.hongfang.ckernet.ai.flowerdetect;

import java.io.InputStream;

/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：Flower.java<br>
 * 描             述 ：花朵物件。<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : yfwong <br>
 * @version  : 0.1 Apr 13, 2019<P>
 */
public class Flower implements Entity {
	String[][] imageBytes;
	public Flower(InputStream inputStream) {
		buildImageBytesBy(inputStream);
	}
	
	private void buildImageBytesBy(InputStream stream) {
		//TODO this.imageBytes = doSomeProcess(stream)
	}
}

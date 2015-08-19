package com.hongfang.ckernel;

import com.hongfang.ckernel.models.internel.ResultBase;

/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：Result.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/7/19<P>
 */
public class Result extends ResultBase {
	
	public final int httpCode;
	
	public Result(int httpCode, String json) {
		super(json);
		this.httpCode = httpCode;
	}
}

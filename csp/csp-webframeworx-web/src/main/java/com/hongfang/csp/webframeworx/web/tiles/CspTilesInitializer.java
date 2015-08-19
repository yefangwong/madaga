/**
 * Copyright (c) 2014 Cornelius.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Cornelius. ("Confidential Information").
 *
 * You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of license agreement you entered
 * into with Cornelius.
 */
package com.hongfang.csp.webframeworx.web.tiles;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.startup.AbstractTilesInitializer;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：CspTilesInitializer.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/4/7<P>
 */
public class CspTilesInitializer extends AbstractTilesInitializer {

	/* (non-Javadoc)
	 * @see org.apache.tiles.startup.AbstractTilesInitializer#createContainerFactory(org.apache.tiles.TilesApplicationContext)
	 */
	@Override
	protected AbstractTilesContainerFactory createContainerFactory(
			TilesApplicationContext arg0) {
		return new CspTilesContainerFactory();
	}

}

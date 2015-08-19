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
package com.hongfang.csp.cbe.location;

import java.io.Serializable;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：Cell.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/9/20<P>
 */
public class Cell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3897101733579110972L;

	private long id;
	
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

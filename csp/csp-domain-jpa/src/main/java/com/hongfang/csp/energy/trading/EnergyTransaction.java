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
package com.hongfang.csp.energy.trading;

import java.io.Serializable;
import java.util.Date;

import com.hongfang.csp.cbe.location.Cell;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：EnergyTransaction.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/9/20<P>
 */
public class EnergyTransaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 98203429833118040L;

	private long transId;          		// 交易代碼
	private Date transDate;		   		// 交易日期
	private Date transStartingTime; 	// 交易開始時間
	private Date transFinishingTime;	// 交易結束時間
	private Cell sellingCell;			// 銷售電力區域 cell
	private Cell consumingCell;         // 消費電力區域 cell 
	private long sendingEnergy;			// 送出電力（kWh)
	private long receivingEnergy;       // 接受電力（kWh)
	private long loss;					// 能源耗損 Sending or Receiving(kWh)
	private long balance;				// ( (送出電力 or 接受電力) - 耗損 )(kWh)
	private double tariff;				// 1 度電買或賣多少錢 (NT/kWh)
	private double moneyBalance;
	
}

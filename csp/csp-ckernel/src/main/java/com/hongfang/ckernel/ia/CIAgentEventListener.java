package com.hongfang.ckernel.ia;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：CIAgentEventListener.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2013/10/7<P>
 */
public interface CIAgentEventListener extends java.util.EventListener {
	public void processCIAgentEvent(CIAgentEvent e);
	
	public void postCIAgentEvent(CIAgentEvent e);
}

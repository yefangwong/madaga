package com.hongfang.ckernel.ia;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：CIAgentEvent.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2013/10/7<P>
 */
@SuppressWarnings("serial")
public class CIAgentEvent extends java.util.EventObject {
	Object argObject;
	private String action = null;
	
	public CIAgentEvent(Object source) {
		super(source);
	}
	
	public CIAgentEvent(CIAgent source) {
		super(source);
	}
	
	public CIAgentEvent(CIAgent source, Object arg) {
		super(source);
		this.argObject = arg;
	}

	public CIAgentEvent(Object source, String action, Object arg) {
		super(source);
		this.action = action;
		this.argObject = arg;
	}
	
	public String getAction() { return action; }
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("CIAgent ");
		buf.append("source: " + source);
		buf.append("action: " + action);
		buf.append("argObject:" + argObject);
		return buf.toString();
	}
}

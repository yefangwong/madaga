package com.hongfang.ckernel.ia;

import java.io.Serializable;
import java.util.Vector;

/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：CIAgentEventQueue.java<br>
 * 描 述 ：<br>
 * 公 司 ：Hongfang intelligent technology.<br>
 * <br>
 * 【 資 料 來 源】 ：<br>
 * 【 輸 出 報 表】 ：<br>
 * 【 異 動 紀 錄】 ：<br>
 * 
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 * 
 * @copyright Constructing Intelligent Agents using Java (C) Joseph P.Bigus and
 *            Jennifer Bigus 1997, 2001
 */
public class CIAgentEventQueue implements Serializable {
	private Vector eventQueue;

	/**
	 * Creates an event queue.
	 */
	public CIAgentEventQueue() {
		eventQueue = new Vector();
	}

	/**
	 * Adds an event to the end of the queue.
	 * 
	 * @param event
	 *            the CIAgentEvent to be added to the queue
	 */
	public synchronized void addEvent(CIAgentEvent event) {
		eventQueue.addElement(event);
	}

	/**
	 * Retrieves the first element from the queue (if any) after removing it
	 * from the queue.
	 * 
	 * @return the first event on the queue
	 */
	public synchronized CIAgentEvent getNextEvent() {
		if (eventQueue.size() == 0) {
			return null;
		} else {
			CIAgentEvent event = (CIAgentEvent) eventQueue.elementAt(0);

			eventQueue.removeElementAt(0);
			return event;
		}
	}

	/**
	 * Retrieves the first element from the queue (if any) without removing it.
	 * 
	 * @return the first event on the queue
	 */
	public synchronized CIAgentEvent peekEvent() {
		if (eventQueue.size() == 0) {
			return null;
		} else {
			return (CIAgentEvent) eventQueue.elementAt(0);
		}
	}
}
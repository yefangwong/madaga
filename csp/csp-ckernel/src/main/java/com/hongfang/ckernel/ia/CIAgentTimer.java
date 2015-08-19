package com.hongfang.ckernel.ia;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：CIAgentTimer.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2013/10/12<P>
 */
public class CIAgentTimer implements Runnable, Serializable {
	private CIAgent agent;
	
	private int sleepTime = 1000;
	private boolean timerEnabled = true;
	private int asyncTime = 500;
	transient private Thread runnit = new Thread(this);
	private boolean quit = false;
	private boolean debug = false;
	
	public CIAgentTimer(CIAgent agent) {
		this.agent = agent;
	}
	
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	public int getSleepTime() {
		return sleepTime;
	}

	public void setAsyncTime(int asyncTime) {
		this.asyncTime = asyncTime;
	}
	
	public int getAsyncTime() {
		return asyncTime;
	}
	
	public void startTimer() {
		timerEnabled = true;
		if(!runnit.isAlive())
			runnit.start();
	}
	
	public void stopTimer() {
		timerEnabled = false;
	}
	
	public void quitTimer() {
		quit = true;
	}
	
	public void run() {
		long startTime = 0;
		long curTime = 0;
		
		if (debug) {
			startTime = new Date().getTime();
			curTime = startTime;
		}
		if (sleepTime < asyncTime) {
			asyncTime = sleepTime;
		}
		int numEventChecks = sleepTime / asyncTime;
		
		if(debug) {
			System.out.println("sleepTime= " + sleepTime + " asyncTime= " +
					asyncTime + "numEventChecks= " +
					numEventChecks);
		}
		while(quit == false) {
			try {
				for(int i = 0; i < numEventChecks; i++) {
					Thread.sleep(asyncTime);
					if (debug) {
						curTime = new Date().getTime();
						System.out.println("async events timer at " +
								(curTime - startTime));
					}
					if (quit) {
						break;
					}
					agent.processAsynchronousEvents();
				}
				if (timerEnabled && (quit == false)) {
					if (debug) {
						curTime = new Date().getTime();
						System.out.println("timer event at " + (curTime - startTime));
					}
					agent.processTimerPop();
				}
			} catch (InterruptedException e){}
		}
	}
	private void readObject(ObjectInputStream theObjectInputStream) 
		throws ClassNotFoundException, IOException {
		runnit = new Thread(this);
		theObjectInputStream.defaultReadObject();
		
	}
}

package com.hongfang.ckernel.ia;

import java.io.Serializable;

public class CIAgentState implements Serializable {
	public static final int UNINITIATED = 0;
	public static final int INITIATED = 1;
	public static final int ACTIVE = 2;
	public static final int SUSPENDED = 3;
	public static final int UNKNOWN = 4;
	private int state;
	
	public CIAgentState() {
		state = UNINITIATED;
	}
	
	public synchronized void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
	public String toString() {
		switch(state) {
			case UNINITIATED :
				return "Uninitiated";
			case INITIATED :
				return "Initiated";
			case ACTIVE :
				return "Active";
			case SUSPENDED :
				return "Suspended";
			case UNKNOWN :
				return "Unknown";
		}
		return "Unknown";
	}
}

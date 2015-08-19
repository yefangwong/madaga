package com.hongfang.ckernel.ia;

import java.util.Vector;

public interface AgentPlatform {

	/**
	 * Returns a list of registered agents.
	 * 
	 * @return the Vector object that contains the agents
	 */
	public Vector getAgents();

	/**
	 * Returns an agent with the specified name.
	 * 
	 * @return the CIAgent object or null if not found
	 */
	public CIAgent getAgent(String name);
}

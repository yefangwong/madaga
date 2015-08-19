package com.hongfang.ckernel.ia;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Vector;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：CIAgent.java<br>
 * 描 述 ：Base class to construct intelligent agent. The <code>CIAgent</code>
 * class defines the common programming interface and behavior for intelligent
 * agents.
 * <p>
 * The lifecycle of a CIAgent is as follows:
 * <ol>
 * <li>construct an agent bean (state = UNINITIATED)
 * <li>set the bean properties
 * <li>call initialize() method (state changes to INITIATED)
 * <li>call startAgentProcessing() method (state changes to ACTIVE) use agent in
 * applications
 * <li>call stopAgentProcessing() method (state changed to UNKNOWN)
 * </ol>
 * <p>
 * Call process() to perform synchronous agent processing of data. Use
 * processTimerPop() to perform asynchronous agent processing.
 * 
 * Call processCIAgentEvent() to perform synchronous event processing Call
 * postCIAgentEvent() to perform asynchronous event processing <br>
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
 * @author : Mark Wong <br>
 * @version : 1.0.0 2013/10/7
 *          <P>
 */
public abstract class CIAgent implements CIAgentEventListener, Serializable {
	public static final int DEFAULT_SLEEPTIME = 15000; // 15 seconds
	public static final int DEFAULT_ASYNCTIME = 1000; // 1 seconds
	protected String name;
	private CIAgentState state = new CIAgentState();
	private CIAgentTimer timer = new CIAgentTimer(this);

	transient private Vector listeners = new Vector(); // list of listeners
	transient private CIAgentEventQueue eventQueue = new CIAgentEventQueue();
	transient private PropertyChangeSupport changes = new PropertyChangeSupport(
			this);

	private boolean traceOn = false;
	protected int traceLevel = 0;
	protected AgentPlatform agentPlatform = null; // agent platform reference

	protected Vector children = new Vector(); // contained agents (if any)
	protected CIAgent parent = null; // container agent (if any)

	/**
	 * Creates a <code>CIAgent</code> instance.
	 */
	public CIAgent() {
		this("CIAgent");
	}

	/**
	 * Creates a CIAgent with specified name.
	 * 
	 * @param name
	 *            the String name given to this agent
	 */
	public CIAgent(String name) {
		this.name = name;
		timer.setAsyncTime(DEFAULT_ASYNCTIME); // set asynchTime
		timer.setSleepTime(DEFAULT_SLEEPTIME); // set sleepTime
		state.setState(CIAgentState.UNINITIATED);
	}

	/**
	 * Sets the name of this agent.
	 * 
	 * @param newName
	 *            the String that contains the new name of this agent
	 */
	public void setName(String newName) {
		String oldName = name;

		name = newName;
		changes.firePropertyChange("name", oldName, name);
	}

	/**
	 * Retrieves the name of this agent.
	 * 
	 * @return the String that contains the name of this agent
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the current state of this agent (UNINITIATED, INITIATED, ACTIVE,
	 * SUSPENDED, UNKNOWN).
	 * 
	 * @param state
	 *            the new state of this agent
	 * 
	 * @param newState
	 *            the int object
	 * 
	 */
	protected void setState(int newState) {
		int oldState = state.getState();

		changes.firePropertyChange("state", oldState, newState);
		this.state.setState(newState);
	}

	/**
	 * Retrieves the current state of this agent (UNINITIATED, INITIATED,
	 * ACTIVE, SUSPENDED, UNKNOWN).
	 * 
	 * @return the state of this agent
	 */
	public CIAgentState getState() {
		return state;
	}

	/**
	 * Sets the agent sleep time (in milliseconds) for autonomous processing.
	 * Note that this has no effect on a running agent.
	 * 
	 * @param sleepTime
	 *            the sleep time (in milliseconds) for this agent
	 */
	public void setSleepTime(int sleepTime) {
		timer.setSleepTime(sleepTime);
	}

	/**
	 * Retrieves the agent sleep time (in milliseconds).
	 * 
	 * @return the sleep time (in milliseconds) for this agent
	 */
	public int getSleepTime() {
		return timer.getSleepTime();
	}

	/**
	 * Sets the time (in milliseconds) for asynchronous event processing. Note
	 * that this has no effect on a running agent.
	 * 
	 * @param asyncTime
	 *            the time (in milliseconds) for asynchronous event processing
	 */
	public void setAsyncTime(int asyncTime) {
		timer.setAsyncTime(asyncTime);
	}

	/**
	 * Retrieves the time (in milliseconds) for asynchronous event processing.
	 * 
	 * @return the time (in milliseconds) for asynchronous event processing
	 */
	public int getAsyncTime() {
		return timer.getAsyncTime();
	}

	/**
	 * Sets the trace level.
	 * 
	 * @param traceLevel
	 *            the integer that represents the trace level
	 */
	public void setTraceLevel(int traceLevel) {
		this.traceLevel = traceLevel;
	}

	/**
	 * Retrieves the trace level.
	 * 
	 * @return the trace level of this agent
	 */
	public int getTraceLevel() {
		return traceLevel;
	}

	/**
	 * Sets the agent platform for this agent.
	 * 
	 * @param agentPlatform
	 *            the AgentPlatform object for this agent
	 */
	public void setAgentPlatform(AgentPlatform agentPlatform) {
		this.agentPlatform = agentPlatform;
	}

	/**
	 * Retrieves the agent platform.
	 * 
	 * @return the agent platform of this agent
	 */
	public AgentPlatform getAgentPlatform() {
		return agentPlatform;
	}

	/**
	 * Retrieves the other agents running on the same agent platform
	 * 
	 * @return a vector of agents running on this platform, null if no platform
	 */
	public Vector getAgents() {
		if (agentPlatform == null) {
			return null;
		} else {
			return agentPlatform.getAgents();
		}
	}

	/**
	 * Retrieves an agent running on the same agent platform
	 * 
	 * @return a CIAgnet running on this platform, null if no match
	 */
	public CIAgent getAgent(String name) {
		if (agentPlatform == null) {
			return null;
		} else {
			return agentPlatform.getAgent(name);
		}
	}

	/**
	 * Retrieves a formatted string for display of this agent's current task.
	 * 
	 * @return the String that represents the current task
	 */
	public abstract String getTaskDescription();

	/**
	 * Retrieves a list of CIAgent objects contained by this agent.
	 * 
	 * @return a Vector of CIAgent objects contained by this agent
	 */
	public Vector getChildren() {
		return (Vector) children.clone();
	}

	/**
	 * Sets the parent CIAgent for this agent.
	 * 
	 * @param parent
	 *            the CIAgent object that is the parent of this agent
	 */
	public void setParent(CIAgent parent) {
		this.parent = parent;
	}

	/**
	 * Retrieves the parent CIAgent object.
	 * 
	 * @return a CIAgent object or null if no parent is defined
	 */
	public CIAgent getParent() {
		return parent;
	}

	/**
	 * Uses introspection on this bean to get the customizer class (if any).
	 * 
	 * @return the customizer class for this agent bean
	 */
	public Class getCustomizerClass() {
		Class customizerClass = null;

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
			BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();

			customizerClass = beanDescriptor.getCustomizerClass();
		} catch (IntrospectionException exc) {
			System.out.println("Can't find customizer bean property " + exc);
		}
		return customizerClass;
	}

	/**
	 * Uses introspection on this bean to get the display name (defaults to the
	 * class name).
	 * 
	 * @return the display name for this agent bean
	 */
	public String getDisplayName() {
		String name = null;

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
			BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();

			name = (String) beanDescriptor.getValue("DisplayName");
		} catch (IntrospectionException exc) {
			System.out.println("Can't find display name bean property " + exc);
		}
		if (name == null) {
			name = this.getClass().getName(); // default to class name
		}
		return name;
	}

	/**
	 * Resets this agent so that it is in a known state.
	 */
	public void reset() {
	}

	/**
	 * Initializes this agent for processing.
	 */
	public abstract void initialize();

	/**
	 * Starts the agent timer and asynchronous event processing and sets the
	 * agent state to ACTIVE.
	 */
	public synchronized void startAgentProcessing() {
		timer.startTimer();
		setState(CIAgentState.ACTIVE);
	}

	/**
	 * Stops the agent timer and asynchronous event processing and sets the
	 * agent state to UNKNOWN.
	 */
	public synchronized void stopAgentProcessing() {

		// stop processing async events and timer events
		// change agent state to unknown
		timer.quitTimer();
		setState(CIAgentState.UNKNOWN);
	}

	/**
	 * Temporarily stops the agent timer so that the autonomous behavior is
	 * suspended and sets the agent state to SUSPENDED.
	 */
	public void suspendAgentProcessing() {

		// turn asynch event processing off
		// turn timer event processing off
		timer.stopTimer();
		setState(CIAgentState.SUSPENDED);
	}

	/**
	 * Resumes agent processing of the timer and asynchronous events and sets
	 * the agent state to ACTIVE.
	 */
	public void resumeAgentProcessing() {

		// turn asynch event processing on
		// turn timer event processing on
		timer.startTimer();
		setState(CIAgentState.ACTIVE);
	}

	/**
	 * Provides the synchronous processing done by this agent.
	 */
	public abstract void process();

	/**
	 * Provides the asynchronous, autonomous behavior of this agent that occurs
	 * periodically, depending on the sleep time for this agent.
	 */
	public abstract void processTimerPop();

	/**
	 * Processes all events on the asynchronous event queue periodically,
	 * depending on the asynchronous event time for this agent.
	 */
	public void processAsynchronousEvents() {
		CIAgentEvent event = null;

		while ((event = eventQueue.getNextEvent()) != null) {
			// System.out.println("CIAgent: " + name +
			// " dispatching an Async event");
			processCIAgentEvent(event);
		}
	}

	/**
	 * Performs synchronous event processing for this agent.
	 * 
	 * @param event
	 *            the CIAgentEvent object
	 */
	public void processCIAgentEvent(CIAgentEvent event) {
	}

	/**
	 * Posts an event to this agent's event queue.
	 * 
	 * @param event
	 *            the CIAgentEvent to be posted
	 */
	public void postCIAgentEvent(CIAgentEvent event) {
		eventQueue.addEvent(event);
	}

	/**
	 * Adds a listener for CIAgent events.
	 * 
	 * @param listener
	 *            the CIAgentEventListener to be added
	 */
	public synchronized void addCIAgentEventListener(
			CIAgentEventListener listener) {
		listeners.addElement(listener);
	}

	/**
	 * Removes a listener for CIAgent events.
	 * 
	 * @param listener
	 *            the CIAgentEventListener to be removed
	 */
	public synchronized void removeCIAgentEventListener(
			CIAgentEventListener listener) {
		listeners.removeElement(listener);
	}

	/**
	 * Delivers the CIAgent event to all registered listeners.
	 * 
	 * @param e
	 *            the CIAgentEvent to be sent to all listeners
	 */
	protected void notifyCIAgentEventListeners(CIAgentEvent e) {
		Vector l;

		synchronized (this) {
			l = (Vector) listeners.clone();
		}
		for (int i = 0; i < l.size(); i++) { // deliver the event
			((CIAgentEventListener) l.elementAt(i)).processCIAgentEvent(e);
		}
	}

	/**
	 * Adds a listener for PropertyChange events.
	 * 
	 * @param listener
	 *            the PropertyChangeListener to be added
	 */
	public synchronized void addPropertyChangeListener(
			PropertyChangeListener listener) {
		changes.addPropertyChangeListener(listener);
	}

	/**
	 * Removes a listener for PropeortyChange events.
	 * 
	 * @param listener
	 *            the PropertyChangeListener to be removed
	 */
	public synchronized void removePropertyChangeListener(
			PropertyChangeListener listener) {
		changes.removePropertyChangeListener(listener);
	}

	/**
	 * Sends a trace event to all registered listeners.
	 * 
	 * @param msg
	 *            the String that is the message portion of the trace event
	 */
	public void trace(String msg) {

		// create a data event
		CIAgentEvent event = new CIAgentEvent(this, "trace", msg);

		// and send it to any registered listeners
		notifyCIAgentEventListeners(event);
	}

	/**
	 * Adds a CIAgent to this agent and sets its parent member to point to this
	 * agent.
	 * 
	 * @param child
	 *            the CIAgent object to be added to this object as a child
	 */
	public void addAgent(CIAgent child) {
		children.addElement(child);
		child.setParent(this);
	}

	/**
	 * Removes a CIAgent from this agent.
	 * 
	 * @param child
	 *            the CIAgent object to be removed from this agent
	 * 
	 */
	public void removeAgent(CIAgent child) {
		children.removeElement(child);
	}

	/**
	 * De-serialize the object from the specified input stream by
	 * re-initializing the object's transient variables, de-serializing the
	 * object with defaultReadObject(), and then hooking up the de-serialized
	 * stuff to the re-initialized stuff.
	 * 
	 * 
	 * @param ois
	 *            the ObjectInputStream object from which this object is to be
	 *            read
	 * 
	 * @exception ClassNotFoundException
	 *                if any class file is not found
	 * @exception IOException
	 *                on any IO error
	 */
	private void readObject(ObjectInputStream theObjectInputStream)
			throws ClassNotFoundException, IOException {
		// Restore the transient variables.
		changes = new PropertyChangeSupport(this);
		listeners = new Vector();
		eventQueue = new CIAgentEventQueue();

		// Restore the rest of the object.
		theObjectInputStream.defaultReadObject();
	}
}
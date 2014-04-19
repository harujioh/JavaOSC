/*
 * Copyright (C) 2003, C. Ramakrishnan / Auracle.
 * All rights reserved.
 *
 * This code is licensed under the BSD 3-Clause license.
 * See file LICENSE (or LICENSE.html) for more information.
 */

package com.illposed.osc.utility;

import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.SimpleOSCListener;

/**
 * @author Chandrasekhar Ramakrishnan
 * @see OSCPacketDispatcher
 */
public class OSCPacketDispatcherTest extends junit.framework.TestCase {

	private OSCPacketDispatcher dispatcher;
	private SimpleOSCListener listener1;
	private SimpleOSCListener listener2;

	@Override
	protected void setUp() throws Exception {
		dispatcher = new OSCPacketDispatcher();
		listener1 = new SimpleOSCListener();
		dispatcher.addListener(new OSCPatternAddressSelector("/listener1"), listener1);
		listener2 = new SimpleOSCListener();
		dispatcher.addListener(new OSCPatternAddressSelector("/listener2"), listener2);
	}

	@Override
	protected void tearDown() throws Exception {

	}

	public void testDispatchToListener1() throws Exception {
		OSCMessage message = new OSCMessage("/listener1");
		dispatcher.dispatchPacket(message);
		if (!listener1.isMessageReceived()) {
			fail("Message to listener1 didn't get sent to listener1");
		}
		if (listener2.isMessageReceived()) {
			fail("Message to listener1 got sent to listener2");
		}
	}

	public void testDispatchToListener2() throws Exception {
		OSCMessage message = new OSCMessage("/listener2");
		dispatcher.dispatchPacket(message);
		if (listener1.isMessageReceived()) {
			fail("Message to listener2 got sent to listener1");
		}
		if (!listener2.isMessageReceived()) {
			fail("Message to listener2 didn't get sent to listener2");
		}
	}

	public void testDispatchToNobody() throws Exception {
		OSCMessage message = new OSCMessage("/nobody");
		dispatcher.dispatchPacket(message);
		if (listener1.isMessageReceived() || listener2.isMessageReceived()) {
			fail("Message to nobody got dispatched incorrectly");
		}
	}

	public void testDispatchBundle() throws Exception {
		OSCBundle bundle = new OSCBundle();
		bundle.addPacket(new OSCMessage("/listener1"));
		bundle.addPacket(new OSCMessage("/listener2"));
		dispatcher.dispatchPacket(bundle);
		if (!listener1.isMessageReceived()) {
			fail("Bundle didn't dispatch message to listener 1");
		}
		if (!listener2.isMessageReceived()) {
			fail("Bundle didn't dispatch message to listener 2");
		}
	}
}

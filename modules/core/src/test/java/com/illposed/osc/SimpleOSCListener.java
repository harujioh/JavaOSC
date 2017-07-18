/*
 * Copyright (C) 2001, C. Ramakrishnan / Illposed Software.
 * All rights reserved.
 *
 * This code is licensed under the BSD 3-Clause license.
 * See file LICENSE (or LICENSE.html) for more information.
 */

package com.illposed.osc;

public class SimpleOSCListener implements OSCMessageListener {

	private boolean messageReceived = false;

	public boolean isMessageReceived() {
		return messageReceived;
	}

	@Override
	public void acceptMessage(OSCMessage message) {
		messageReceived = true;
	}
}

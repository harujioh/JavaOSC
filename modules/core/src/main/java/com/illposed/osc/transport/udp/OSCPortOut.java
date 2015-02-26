/*
 * Copyright (C) 2004-2014, C. Ramakrishnan / Illposed Software.
 * All rights reserved.
 *
 * This code is licensed under the BSD 3-Clause license.
 * See file LICENSE (or LICENSE.html) for more information.
 */

package com.illposed.osc.transport.udp;

import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCSerializer;
import com.illposed.osc.OSCSerializerFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Sends OSC packets to a specific TCP/IP address and port.
 *
 * To send an OSC message, call {@link #send(OSCPacket)}.
 *
 * An example:<br>
 * (loosely based on {com.illposed.osc.OSCPortTest#testMessageWithArgs()})
 * <blockquote><pre>{@code
 * OSCPortOut sender = new OSCPortOut();
 * List<Object> args = new ArrayList<Object>(2);
 * args.add(3);
 * args.add("hello");
 * OSCMessage msg = new OSCMessage("/sayhello", args);
 * try {
 * 	sender.send(msg);
 * } catch (Exception ex) {
 * 	System.err.println("Couldn't send");
 * }
 * }</pre></blockquote>
 */
public class OSCPortOut extends OSCPort {

	private final InetAddress address;
	private final ByteArrayOutputStream outputBuffer;
	private final OSCSerializer converter;

	/**
	 * Create an OSCPort that sends to address:port using a specified socket
	 * and a specified serializer.
	 * @param serializerFactory the UDP address to send to
	 * @param address the UDP address to send to
	 * @param port the UDP port to send to
	 * @param socket the DatagramSocket to send from
	 */
	public OSCPortOut(
			final OSCSerializerFactory serializerFactory,
			final InetAddress address,
			final int port,
			final DatagramSocket socket)
	{
		super(socket, port);
		this.address = address;
		this.outputBuffer = new ByteArrayOutputStream();
		this.converter = serializerFactory.create(outputBuffer);
	}

	/**
	 * Create an OSCPort that sends to address:port using a specified socket.
	 * @param address the UDP address to send to
	 * @param port the UDP port to send to
	 * @param socket the DatagramSocket to send from
	 */
	public OSCPortOut(final InetAddress address, final int port, final DatagramSocket socket) {
		this(OSCSerializerFactory.createDefaultFactory(), address, port, socket);
	}

	/**
	 * Create an OSCPort that sends to address:port.
	 * @param address the UDP address to send to
	 * @param port the UDP port to send to
	 * @throws SocketException when failing to create a (UDP) out socket
	 */
	public OSCPortOut(final InetAddress address, final int port) throws SocketException {
		this(address, port, new DatagramSocket());
	}

	/**
	 * Create an OSCPort that sends to address,
	 * using the standard SuperCollider port.
	 * @param address the UDP address to send to
	 * @throws SocketException when failing to create a (UDP) out socket
	 */
	public OSCPortOut(final InetAddress address) throws SocketException {
		this(address, DEFAULT_SC_OSC_PORT);
	}

	/**
	 * Create an OSCPort that sends to "localhost",
	 * on the standard SuperCollider port.
	 * @throws UnknownHostException if the local host name could not be resolved into an address
	 * @throws SocketException when failing to create a (UDP) out socket
	 */
	public OSCPortOut() throws UnknownHostException, SocketException {
		this(InetAddress.getLocalHost(), DEFAULT_SC_OSC_PORT);
	}

	/**
	 * Send an OSC packet (message or bundle) to the receiver we are bound to.
	 * @param aPacket the bundle or message to send
	 * @throws IOException if a (UDP) socket I/O error occurs
	 */
	public void send(final OSCPacket aPacket) throws IOException {
		outputBuffer.reset();
		converter.write(aPacket);
		final byte[] byteArray = outputBuffer.toByteArray();
		final DatagramPacket packet =
				new DatagramPacket(byteArray, byteArray.length, address, getPort());
		getSocket().send(packet);
	}
}
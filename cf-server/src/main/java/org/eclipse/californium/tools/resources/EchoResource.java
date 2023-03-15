/*******************************************************************************
 * Copyright (c) 2015 Institute for Pervasive Computing, ETH Zurich and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *    Matthias Kovatsch - creator and main architect
 ******************************************************************************/
package org.eclipse.californium.tools.resources;

import java.net.InetSocketAddress;
import java.util.Queue;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.eclipse.californium.tools.interceptors.SendDelayedCommandInterceptor;

/**
 * This resource responds with the data from a request in its payload. This
 * resource responds to GET, POST, PUT and DELETE requests.
 */
public class EchoResource extends CoapResource {

	private Queue<String>	cmdQueue;

	public EchoResource(String name, Queue<String> cmdQueue) {
		super(name, true);

		this.cmdQueue = cmdQueue;
	}
	
	@Override
	public Resource getChild(String name) {
		return this;
	}
	
	/**
	 * This method uses the internal {@link Exchange} class for advanced handling.
	 */
	@Override
	public void handleRequest(Exchange exchange) {
		Request request = exchange.getRequest();
		InetSocketAddress source = request.getSourceContext().getPeerAddress();
		StringBuilder buffer = new StringBuilder();
		String payloadStr = request.getPayloadString();
		buffer.append("resource ").append(getURI()).append(" received request")
				.append("\n").append("Code: ").append(request.getCode())
				.append("\n").append("Source: ").append(source.getAddress()).append(":").append(source.getPort())
				.append("\n").append("Type: ").append(request.getType())
				.append("\n").append("MID: ").append(request.getMID())
				.append("\n").append("Token: ").append(request.getTokenString())
				.append("\n").append("REQUEST BODY: ").append(payloadStr)
				.append("\n").append(request.getOptions());

		if (payloadStr.startsWith("HAS_CMD")) {
			cmdQueue.add(payloadStr + System.currentTimeMillis());
			SendDelayedCommandInterceptor.HAS_CMD_FLAG.set(true);
		} else {
			SendDelayedCommandInterceptor.HAS_CMD_FLAG.set(false);
		}

		Response response = new Response(ResponseCode.CONTENT);
		response.setPayload(buffer.toString());
		response.getOptions().setContentFormat(MediaTypeRegistry.TEXT_PLAIN);
		exchange.sendResponse(response);
	}
}

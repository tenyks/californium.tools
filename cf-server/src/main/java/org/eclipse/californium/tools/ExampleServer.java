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
package org.eclipse.californium.tools;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.elements.config.Configuration;
import org.eclipse.californium.elements.config.UdpConfig;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConfig;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.tools.resources.*;
import org.eclipse.californium.tools.utils.SecureEndpointPool;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;

/**
 * This is an example server that contains a few resources for demonstration.
 */
public class ExampleServer {

	/**
	 * File name for configuration.
	 */
	private static final File CONFIG_FILE = new File("CaliforniumExampleDemo.properties");
	/**
	 * Header for configuration.
	 */
	private static final String CONFIG_HEADER = "Californium CoAP Properties file for Example-Server";



	public static void main(String[] args) throws Exception {
		CoapConfig.register();
		UdpConfig.register();
		DtlsConfig.register();

		Configuration config = Configuration.createWithFile(CONFIG_FILE, CONFIG_HEADER, null);

		CoapServer server = new CoapServer(config);

		server.add(new HelloWorldResource("hello"));
		server.add(new FibonacciResource("fibonacci"));
		server.add(new StorageResource("storage"));
		server.add(new ImageResource("image"));
		server.add(new EchoResource("echo"));
		server.add(new LargeResource("large"));

		//个人练习增加的
		server.add(new WritableResource("writeMe", "To Be Replaced!"));
		server.add(new TimeResource("time"));
		server.add(new RemovableResource("removeMe", "To Be Deleted!"));
		server.add(new ObservableResource("observeMe"));

		server.addEndpoint(buildSecurityEndpoint(config, null));

		server.start();
	}

	private static CoapEndpoint	buildSecurityEndpoint(Configuration config, Integer port) throws IOException, GeneralSecurityException {
		if (port == null) {
			port = config.get(CoapConfig.COAP_SECURE_PORT);
		}

		DtlsConnectorConfig.Builder builder = SecureEndpointPool.setupServer(config);
		builder.setAddress(new InetSocketAddress(port));
		DTLSConnector connector = new DTLSConnector(builder.build());
		CoapEndpoint endpoint = CoapEndpoint.builder().setConfiguration(config).setConnector(connector).build();

		return endpoint;
	}
	
	/*
	 *  Sends a GET request to itself
	 */
	public static void selfTest() {
		try {
			Request request = Request.newGet();
			request.setURI("coap://localhost:5683/hello");
			request.send();
			Response response = request.waitForResponse(1000);
			System.out.println("received "+response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

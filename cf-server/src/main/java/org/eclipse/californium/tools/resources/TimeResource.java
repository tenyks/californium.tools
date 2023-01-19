package org.eclipse.californium.tools.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 * @author 李仲允
 * @date 2023/1/19 10:44
 */
public class TimeResource extends CoapResource {

    public TimeResource(String name) {
        super(name);
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.respond(String.valueOf(System.currentTimeMillis()));
    }
}

package org.eclipse.californium.tools.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.nio.charset.StandardCharsets;

/**
 * @author 李仲允
 * @date 2023/1/19 10:30
 */
public class WritableResource extends CoapResource {

    public String value = "to be replaced";

    public WritableResource(String name, String initValue) {
        super(name);

        if (initValue != null) this.value = initValue;
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.respond(value);
    }

    @Override
    public void handlePUT(CoapExchange exchange) {
        value = new String(exchange.getRequestPayload(), StandardCharsets.UTF_8);
        exchange.respond(CoAP.ResponseCode.CHANGED, value);
    }
}

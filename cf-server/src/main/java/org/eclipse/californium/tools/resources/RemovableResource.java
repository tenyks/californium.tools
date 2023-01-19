package org.eclipse.californium.tools.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 * @author 李仲允
 * @date 2023/1/19 10:47
 */
public class RemovableResource extends CoapResource {

    private String  value;

    public RemovableResource(String name, String value) {
        super(name);

        this.value = value;
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.respond(value);
    }

    @Override
    public void handleDELETE(CoapExchange exchange) {
        delete();

        exchange.respond(CoAP.ResponseCode.DELETED, value);
    }
}

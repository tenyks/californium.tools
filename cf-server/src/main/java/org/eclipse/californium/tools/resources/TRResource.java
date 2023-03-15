package org.eclipse.californium.tools.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TRResource extends CoapResource {

    private static final Logger log = LoggerFactory.getLogger(TRResource.class);

    public TRResource(String name) {
        super(name, true);
    }

    @Override
    public void execute(Runnable task) {
        super.execute(task);
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        String ep = exchange.getQueryParameter("ep");
        log.debug("ep={}", ep);

        Response response = new Response(CoAP.ResponseCode.CHANGED);
        exchange.respond(response);
    }
}

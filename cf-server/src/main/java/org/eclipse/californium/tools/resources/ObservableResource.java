package org.eclipse.californium.tools.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.observe.ObserveRelation;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 * @author 李仲允
 * @date 2023/1/19 16:46
 */
public class ObservableResource extends CoapResource {

    public ObservableResource(String name) {
        super(name);

        super.setObservable(true);
    }

    @Override
    public void addObserveRelation(ObserveRelation relation) {
        super.addObserveRelation(relation);

        System.out.printf("Add ObserveRelation(%s)", relation);
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        //31171260128120
        exchange.respond(String.format("C=%d,%d", getObserverCount(), System.currentTimeMillis()));
    }
}

package org.eclipse.californium.tools;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.EmptyMessage;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.interceptors.MessageInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 李仲允
 * @date 2023/3/14 22:27
 */
public class SendDelayedCommandInterceptor implements MessageInterceptor {

    public static final AtomicBoolean HAS_CMD_FLAG = new AtomicBoolean(false);

    public static final Logger log = LoggerFactory.getLogger(SendDelayedCommandInterceptor.class);

    private Endpoint    refEndpoint;

    public SendDelayedCommandInterceptor(Endpoint refEndpoint) {
        this.refEndpoint = refEndpoint;
    }

    @Override
    public void sendRequest(Request request) {

    }

    @Override
    public void sendResponse(Response response) {
        if (!HAS_CMD_FLAG.get()) {
            log.debug("[CMD]无待下发的指令");
            return ;
        }

        Request cmdReq = new Request(CoAP.Code.POST, CoAP.Type.CON);
        InetSocketAddress peerAddr = response.getDestinationContext().getPeerAddress();
        String uri = String.format("coap://%s:%d/cmd", peerAddr.getHostString(), peerAddr.getPort());
        cmdReq.setURI(uri);
        cmdReq.setPayload("HELLO WORLD!");

        log.debug("uri={}, cmdReq={}", uri, cmdReq);
        refEndpoint.sendRequest(cmdReq);
    }

    @Override
    public void sendEmptyMessage(EmptyMessage message) {

    }

    @Override
    public void receiveRequest(Request request) {

    }

    @Override
    public void receiveResponse(Response response) {

    }

    @Override
    public void receiveEmptyMessage(EmptyMessage message) {

    }
}

package org.eclipse.californium.tools.interceptors;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.EmptyMessage;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.interceptors.MessageInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

public class SendDelayedCommandInterceptor implements MessageInterceptor {

    public static AtomicBoolean HAS_CMD_FLAG = new AtomicBoolean(false);


    private static final Logger log = LoggerFactory.getLogger(SendDelayedCommandInterceptor.class);

    private Endpoint refEndpoint;

    private Queue<String>   cmdQueue;

    private ExecutorService     execSvc;

    public SendDelayedCommandInterceptor(Endpoint refEndpoint, Queue<String> cmdQueue) {
        this.refEndpoint = refEndpoint;
        this.cmdQueue = cmdQueue;
        this.execSvc = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void sendRequest(Request request) {

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

    @Override
    public void sendResponse(Response response) {
        if (cmdQueue.isEmpty()) {
            log.debug("[Command]无待下发的指令。");
            return ;
        }

        log.debug("[Command]可传输待下发的指令：");

        final String rspPayload = cmdQueue.poll();
        final InetSocketAddress peerAddress = response.getDestinationContext().getPeerAddress();
        final String uri = String.format("coap://%s:%d/rd/1001", peerAddress.getHostString(), peerAddress.getPort());

        execSvc.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("异常", e);
                }

                Request cmdReq = new Request(CoAP.Code.PUT, CoAP.Type.CON);

                cmdReq.setURI(uri);
                cmdReq.setPayload(rspPayload);

                log.debug("[Command]即将下发指令数据：uri={}, payload={}", uri, rspPayload);
                refEndpoint.sendRequest(cmdReq);
            }
        });
    }

    public void sendResponse2(Response response) {
        if (!HAS_CMD_FLAG.get()) {
            log.debug("[Command]无待下发的指令。");
            return ;
        }

        log.debug("[Command]可传输待下发的指令：");

        Request cmdReq = new Request(CoAP.Code.PUT, CoAP.Type.CON);
        InetSocketAddress peerAddress = response.getDestinationContext().getPeerAddress();
        cmdReq.setURI(String.format("coap://%s:%d/rd/1001", peerAddress.getHostString(), peerAddress.getPort()));
        cmdReq.setPayload("Hello");

        refEndpoint.sendRequest(cmdReq);
    }
}

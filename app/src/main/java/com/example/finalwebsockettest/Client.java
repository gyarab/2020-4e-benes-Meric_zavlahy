package com.example.finalwebsockettest;

import android.util.Log;

import java.net.URI;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

//http://github.com/TooTallNate/Java-WebSocket

public class Client extends WebSocketClient {

    public Client(URI serverUri, Draft draft) {
        super(serverUri, draft);

    }

    public Client(URI serverURI) {
        super(serverURI);

    }

    public Client(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }


    public void onOpen(ServerHandshake handshakedata) {
        Log.d("client", "opened connection");
    }


    public void onMessage(String message) {
        Log.d("received", "received: " + message);
    }


    public void onClose(int code, String reason, boolean remote) {
        Log.d("onClose",
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }


    public void onError(Exception ex) {
        ex.printStackTrace();
    }


}
package com.example.finalwebsockettest;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

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
        send("Data request");
        Log.d("client", "opened connection");
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }


    public void onMessage(String message) {
        Log.d("received","received: " + message);


    }



    public void onClose(int code, String reason, boolean remote) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        Log.d("onClose",
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }


    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }


}
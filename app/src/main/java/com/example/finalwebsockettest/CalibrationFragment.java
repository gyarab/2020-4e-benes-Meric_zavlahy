package com.example.finalwebsockettest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class CalibrationFragment extends Fragment {
    //Po kliknutí na tlačítko s označením aktuálního stavu vlhkosti(WET/DRY) načtě aktuální vlhkost z databáze,
    //následně je označený zaslán zpět serveru a uložen do databáze

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.activity_calibration, null);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        int sensorID = 1;   //zatím napevno nastavené pouze pro 1 senzor
        SharedPreferences sharedPref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);


        view.findViewById(R.id.dry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WorkRequest uploadWorkRequest =
                        new OneTimeWorkRequest.Builder(CheckMoisture.class)
                                .build();
                WorkManager.getInstance(getContext()).enqueue(uploadWorkRequest);

                int dryMoisture = sharedPref.getInt("moisturee", 999);
                Client c = null;
                try {
                    c = new Client(new URI(
                            "wss://young-stream-00076.herokuapp.com/calibration"), new Draft_6455()) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            super.onOpen(handshakedata);
                            send(sensorID + ":" + dryMoisture + ":" + "10");        //ID senzoru : vlhkost : typ calibrace - dry
                        }

                        public void onMessage(String message) {
                        }
                    };
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                c.connect();
            }
        });

        view.findViewById(R.id.wet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WorkRequest uploadWorkRequest =
                        new OneTimeWorkRequest.Builder(CheckMoisture.class)
                                .build();
                WorkManager.getInstance(getContext()).enqueue(uploadWorkRequest);

                int wetMoisture = sharedPref.getInt("moisturee", 999);
                Client c = null;
                try {
                    //ws://192.168.1.184:80
                    c = new Client(new URI(
                            "wss://young-stream-00076.herokuapp.com/calibration"), new Draft_6455()) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            super.onOpen(handshakedata);
                            send(sensorID + ":" + wetMoisture + ":" + "11");        //ID senzoru : vlhkost : typ calibrace - wet
                        }

                        public void onMessage(String message) {
                        }
                    };
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                c.connect();
            }
        });
    }
}

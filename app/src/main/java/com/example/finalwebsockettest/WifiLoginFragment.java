package com.example.finalwebsockettest;

import android.net.Network;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//Připojí telefon k Acess pointu vytvořeného senzorem,
//Přesměruje uživatele na webovou stránku, kde se přihlásí na svoji wifi sít

public class WifiLoginFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_wifi_login, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button connectB = (Button) view.findViewById(R.id.connect_button);      //https://stackoverflow.com/questions/22390942/onclick-listener-not-working-getting-no-errors
        Button configB = (Button) view.findViewById(R.id.config_button);

        connectB.setOnClickListener(this);
        configB.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override


    public void onClick(View view) {
        switch (view.getId()) {
            //Po kliknutí na tlačítko CONNECT připojí telefon k Acess pointu vytvořeného senzorem
            case R.id.connect_button:

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    final NetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                            .setSsid("MoistureSensor")
                            .setWpa2Passphrase("vKN*5ZGMkX")
                            .build();

                    final NetworkRequest request = new NetworkRequest.Builder()
                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .setNetworkSpecifier(specifier)
                            .build();

                    final ConnectivityManager connectivityManager =
                            (ConnectivityManager) getActivity().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {

                        public void onAvailable(Network network) {
                            connectivityManager.bindProcessToNetwork(network);
                        }
                    };

                    connectivityManager.requestNetwork(request, networkCallback);
                    break;
                }
            //Po kliknutí na tlačítko CHOOSE NETWORK přesměruje uživatele na webovou stránku, kde se přihlásí na svoji wifi sít
            case R.id.config_button:
                Intent intent = new Intent(view.getContext(), activity_redirect_webview.class);
                startActivity(intent);
                break;
        }
    }
}
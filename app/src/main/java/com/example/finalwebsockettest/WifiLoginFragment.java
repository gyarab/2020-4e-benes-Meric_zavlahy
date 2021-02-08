package com.example.finalwebsockettest;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.PatternMatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.java_websocket.drafts.Draft_6455;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


public class WifiLoginFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wifi_login, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.connect_button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                  /*  WifiManager wifiManager = (WifiManager)getContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String Mainwifissid = wifiInfo.getSSID();
*/
                    final NetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                                    .setSsid("AutoConnectAP")
                                    .setWpa2Passphrase("Semetrika716")
                                    .build();

                    final NetworkRequest request = new NetworkRequest.Builder()
                                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                                    .setNetworkSpecifier(specifier)
                                    .build();

                    final ConnectivityManager connectivityManager =
                            (ConnectivityManager) getActivity().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {

                        public void onAvailable (Network network){
                            connectivityManager.bindProcessToNetwork(network);

                            Network ns = connectivityManager.getActiveNetwork();
                            NetworkCapabilities c = connectivityManager.getNetworkCapabilities(ns);
                            Log.d("NetworkCapa", "inspectNetworks: network="+ns+" capabilities="+c); // <- breakpoint

                        }
                    };

                    connectivityManager.requestNetwork(request, networkCallback);

            }
                NavHostFragment.findNavController(WifiLoginFragment.this)
                        .navigate(R.id.action_WifiLoginFragment_to_setupWifiFragment);



                                /*
                final WifiNetworkSuggestion suggestion1 =
                        new WifiNetworkSuggestion.Builder()
                                .setSsid("AutoConnectAP")
                                .setIsAppInteractionRequired(true) // Optional (Needs location permission)
                                .build();


                final List<WifiNetworkSuggestion> suggestionsList = new ArrayList<WifiNetworkSuggestion>();
                    suggestionsList.add(suggestion1);

                final WifiManager wifiManager =
                        (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);

                final int status = wifiManager.addNetworkSuggestions(suggestionsList);
                if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
// do error handling here…
                }

// Optional (Wait for post connection broadcast to one of your suggestions)
                final IntentFilter intentFilter =
                        new IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION);

                final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (!intent.getAction().equals(
                                WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) {
                            return;
                        }
                        // do post connect processing here...
                    }
                };
                getContext().registerReceiver(broadcastReceiver, intentFilter);

                */
            }
        });
    }
}
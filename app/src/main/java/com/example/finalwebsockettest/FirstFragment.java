package com.example.finalwebsockettest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FirstFragment extends Fragment {

    String TextMoisture;
    String TextTime;

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (key.equals("time")) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_self);
            }
        }
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, null);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        TextView textView = (TextView)view.findViewById(R.id.textview_value);
        TextView timeView = (TextView)view.findViewById(R.id.timeTextView);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        TextMoisture = sharedPref.getString("moisture", "Unknown Value");
        TextTime = sharedPref.getString("time", "UknownTime");

        sharedPref.registerOnSharedPreferenceChangeListener(listener);

        textView.setText(TextMoisture);
        timeView.setText(TextTime);




        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WorkRequest uploadWorkRequest =
                        new OneTimeWorkRequest.Builder(CheckMoisture.class)
                                .build();
                WorkManager.getInstance(getContext()).enqueue(uploadWorkRequest);
            }
        });

        view.findViewById(R.id.wifimanager_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_WifiLoginFragment);

            }
        });

        }


    }

package com.example.finalwebsockettest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


public class FirstFragment extends Fragment {

    String TextTime;

    //Pokud se uložený čas v SharedPreferences změní, načte znovu FirstFragment s aktuálními hodnotami
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
        View view = inflater.inflate(R.layout.fragment_first, null);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        TextView textView = (TextView) view.findViewById(R.id.textview_value);
        TextView timeView = (TextView) view.findViewById(R.id.timeTextView);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        int moistureInPercentage = sharedPref.getInt("moistureInPercentage", 0);
        TextTime = sharedPref.getString("time", "UknownTime");
        sharedPref.registerOnSharedPreferenceChangeListener(listener);

        textView.setText(Integer.toString(moistureInPercentage) + "%");
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


    }


}

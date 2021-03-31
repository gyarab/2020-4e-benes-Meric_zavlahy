package com.example.finalwebsockettest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

//Uloží nastavení do SharedPreferences

public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        EditTextPreference moistureLimit = findPreference("moisture_limit");     //limit pro notifikaci
        EditTextPreference checkInterval = findPreference("check_interval");     //interval kontroly vlhkosti
        SharedPreferences sharedPref = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("moistureLimit", moistureLimit.getText());
        editor.putString("check_interval", checkInterval.getText());
        editor.apply();
    }


}



package com.example.finalwebsockettest;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

//Aktivita pro calibraci

public class CalibrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Calibration");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(), R.color.toolbarGreen, null)));
        setTheme(R.style.SettingsFragmentStyle);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new CalibrationFragment())
                .commit();
    }

}
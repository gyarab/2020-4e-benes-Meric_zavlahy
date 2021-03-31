package com.example.finalwebsockettest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.util.Log;
import android.view.MenuInflater;

import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.TimeUnit;


//Hlavní aktivita, zapne se vždy při spuštění a aktivuje PeriodicWorkera, který načítá vlhkost ze serveru v intervalu nastaveném uživatelem


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedSettings = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        int repeatInterval = Integer.parseInt(sharedSettings.getString("check_interval", "30"));
        Log.d("interval", "New Worker with Interval: " + repeatInterval);

        WorkRequest PeriodicWorkRequest =
                new PeriodicWorkRequest.Builder(CheckMoisture.class, repeatInterval, TimeUnit.MINUTES)
                        .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("PMCH",
                ExistingPeriodicWorkPolicy.KEEP,
                (androidx.work.PeriodicWorkRequest) PeriodicWorkRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    //Jednotlivé akce v menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_calibration) {
            Intent intent = new Intent(MainActivity.this, CalibrationActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_wifiSetup) {
            Intent intent = new Intent(MainActivity.this, WifiSetupActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
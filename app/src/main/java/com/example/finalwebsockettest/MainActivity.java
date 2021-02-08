package com.example.finalwebsockettest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.util.Log;
import android.view.MenuInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.TimeUnit;

import static com.example.finalwebsockettest.NotificationUtils.ANDROID_CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedSettings = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        int repeatInterval = sharedSettings.getInt("interval", 15);
        Log.d("interval", "New Worker with Interval: " + repeatInterval);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPref.registerOnSharedPreferenceChangeListener(listener);

        WorkRequest PeriodicUploadWorkRequest =
                new PeriodicWorkRequest.Builder(CheckMoisture.class, repeatInterval, TimeUnit.MINUTES)
                        .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(PeriodicUploadWorkRequest);

    }
/*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        //SharedPreferences sharedSettings = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            Float realMoisture = Float.valueOf((prefs.getString("moisture", "10.2")));
            Log.d("MoistureChange","MoistureChange happend>>>  " + realMoisture);
            if (key.equals("moisture") && realMoisture < 2.0)  {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("Hey, your plant needs water!")
                        .setContentText("Moisture: " + realMoisture)
                        .setColor(Color.GREEN)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                Log.d("MoistureChangeNotification","SendingNotification>>>  " + realMoisture);
                notificationManager.notify(101, builder.build());
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
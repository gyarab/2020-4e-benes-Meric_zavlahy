package com.example.finalwebsockettest;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

//Přesměruje uživatele na WifiLoginFragment a nastaví toolbar

public class WifiSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Wifi Setup");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(), R.color.toolbarGreen, null)));
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new WifiLoginFragment())
                .commit();
    }
}
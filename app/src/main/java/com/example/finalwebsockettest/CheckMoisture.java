package com.example.finalwebsockettest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.java_websocket.drafts.Draft_6455;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckMoisture extends Worker {
    public CheckMoisture(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    public Result doWork(){
        Client c = null; // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
        try {
            c = new Client(new URI(
                    "ws://192.168.208.10:8080"), new Draft_6455()) {
                @Override
                public void onMessage(String message) {
                    super.onMessage(message);
                    Log.d("Worker","Running, message: " + message);
                    Calendar.getInstance().getTime();

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
                    editor.putString("moisture",message);
                    editor.putString("time", sdf.format(Calendar.getInstance().getTime()));
                    editor.apply();

                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return Result.failure();
        }
        c.connect();
        return Result.success();
    }
}

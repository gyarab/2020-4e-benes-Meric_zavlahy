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
            //ws://192.168.1.184:80
            c = new Client(new URI(
                    "wss://young-stream-00076.herokuapp.com/client"), new Draft_6455()) {
                @Override
                public void onMessage(String message) {
                    super.onMessage(message);
                    Log.d("Worker","Running, message: " + message);
                    String[] messageArr = message.split(":", 3);
                    int moisture = Integer.parseInt(messageArr[0]);
                    int configWet = Integer.parseInt(messageArr[1]);
                    int configDry = Integer.parseInt(messageArr[2]);
                    Calendar.getInstance().getTime();

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
                    editor.putInt("moisturee", moisture);
                    editor.putInt("configWet", configWet);
                    editor.putInt("configDry", configDry);
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

package com.example.finalwebsockettest;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.finalwebsockettest.NotificationUtils.ANDROID_CHANNEL_ID;

//zašle serveru ID senzoru a dostane nazpět aktuální vlhkost a nastavení
//podle nastavení převede vlhkost na procenta a všechny údaje uloží do "SharedPreferences"
//pokud je vlhkost nižší než limit, který si uživatel zvolil pošle notifikaci

public class CheckMoisture extends Worker {
    int sensorID = 1; //zatím napevno nastavené pouze pro senzor s ID 1
    private NotificationUtils mNotificationUtils;

    public CheckMoisture(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public Result doWork() {
        Client c = null;
        try {
            c = new Client(new URI(
                    "wss://young-stream-00076.herokuapp.com/client"), new Draft_6455()) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    super.onOpen(handshakedata);
                    send(String.valueOf(sensorID));        //ID senzoru
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onMessage(String message) {
                    super.onMessage(message);

                    String[] messageArr = message.split(":", 3);
                    int moisture = Integer.parseInt(messageArr[0]);
                    int configWet = Integer.parseInt(messageArr[1]);
                    int configDry = Integer.parseInt(messageArr[2]);
                    Calendar.getInstance().getTime();

                    //převod na procenta
                    float x = (moisture - configWet);
                    float Udiff = (configDry - configWet);
                    float U1 = Udiff / 100;

                    float z = (configDry - moisture) / U1;
                    int moistureInPercentage = Math.round(z);
                    if (moistureInPercentage < 0) {
                        moistureInPercentage = 0;
                    }
                    Log.d("convertssss", "moisture  " + moisture + "configWet " + configWet + " configDry " + configDry);
                    Log.d("convertssss", " z " + z + " Udiff " + Udiff + " U1 " + U1 + " final " + moistureInPercentage + "moisture " + moisture); // <- breakpoint


                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    editor.putInt("moisturee", moisture);
                    editor.putInt("configWet", configWet);
                    editor.putInt("configDry", configDry);
                    editor.putInt("moistureInPercentage", moistureInPercentage);
                    editor.putString("time", sdf.format(Calendar.getInstance().getTime()));
                    editor.apply();

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String moistureLimitStr = prefs.getString("moisture_limit", "50");
                    int moistureLimit = Integer.parseInt(moistureLimitStr);

                    //Notifikace
                    mNotificationUtils = new NotificationUtils(getApplicationContext());

                    if (moistureInPercentage < moistureLimit) {
                        Notification.Builder builder = mNotificationUtils.
                                getAndroidChannelNotification(moistureInPercentage);
                        mNotificationUtils.getManager().notify(101, builder.build());
                    }
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

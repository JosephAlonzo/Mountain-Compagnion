package com.zekri.yassine.wear.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zekri.yassine.linked.Constants;
import com.zekri.yassine.wear.utils.DailyHeartbeat;

import java.util.HashMap;
import java.util.Map;

import static android.hardware.Sensor.TYPE_HEART_RATE;
import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

/**
 * Created by Zekri on 11/02/2018.
 */

public class WearHeartbeatService extends Service implements SensorEventListener{

    public static final String TAG = "WearHeartbeatService";

    private SensorManager mSensorManager;
    private Sensor mCountSensor;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mCountSensor = mSensorManager.getDefaultSensor(TYPE_HEART_RATE);
        boolean bool = mSensorManager.registerListener(this, mCountSensor, SENSOR_DELAY_NORMAL);
        Log.d(TAG, String.valueOf(bool));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType() == TYPE_HEART_RATE) {
            DailyHeartbeat.updateHeartbeat((int)sensorEvent.values[0]);
            sendHeartbeatCountUpdate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

        sendHeartbeatCountUpdate();
    }

    private void sendHeartbeatCountUpdate() {

        Intent intent = new Intent();
        intent.setAction(Constants.HEARTBEAT_COUNT_MESSAGE);
        intent.putExtra(Constants.HEARTBEAT_COUNT_VALUE, DailyHeartbeat.getHeartbeat());
        sendBroadcast(intent);
    }

}


package com.zekri.yassine.wear.service;

import android.content.Intent;

import com.zekri.yassine.wear.tasks.RandomHeartbeatCounterTimerTask;

import java.util.Timer;

/**
 * Created by Zekri on 11/02/2018.
 */

public class WearHeartbeatEmulatorService extends WearHeartbeatService {

    public static final String TAG = "WearHearEmulatorService";

    private static Timer mTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(mTimer == null) {

            mTimer = new Timer();
            long delay = 1000;
            long period = 1000;
            mTimer.scheduleAtFixedRate(new RandomHeartbeatCounterTimerTask(getApplicationContext()), delay, period);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        mTimer = null;
        super.onDestroy();
    }
}

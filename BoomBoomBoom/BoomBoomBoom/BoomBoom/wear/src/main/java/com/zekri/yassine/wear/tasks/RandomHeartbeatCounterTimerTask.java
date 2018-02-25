package com.zekri.yassine.wear.tasks;

import android.content.Context;
import android.content.Intent;

import com.zekri.yassine.linked.Constants;
import com.zekri.yassine.wear.utils.DailyHeartbeat;

import java.util.TimerTask;

/**
 * Created by Zekri on 11/02/2018.
 */

public class RandomHeartbeatCounterTimerTask extends TimerTask {

    public static final String TAG = "RandomHeartbeatCounterTimerTask";

    private static int heartbeatCount;

    private Context mContext;

    public RandomHeartbeatCounterTimerTask(Context context) {
        this.mContext = context;
        heartbeatCount = 50 + (int) (Math.random() * 200);
        DailyHeartbeat.updateHeartbeat(heartbeatCount);
    }


    @Override
    public void run() {
        heartbeatCount = 50 + (int) (Math.random() * 200);
        DailyHeartbeat.updateHeartbeat(heartbeatCount);
        Intent intent = new Intent();
        intent.setAction(Constants.HEARTBEAT_COUNT_MESSAGE);
        intent.putExtra(Constants.HEARTBEAT_COUNT_VALUE, DailyHeartbeat.getHeartbeat());
        mContext.sendBroadcast(intent);
    }
}

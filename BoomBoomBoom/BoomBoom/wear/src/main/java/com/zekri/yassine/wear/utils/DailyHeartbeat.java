package com.zekri.yassine.wear.utils;

/**
 * Created by Zekri on 11/02/2018.
 */

public class DailyHeartbeat {
    private static int heartbeat;

    public static void updateHeartbeat(int h) {
        heartbeat = h;
    }

    public static int getHeartbeat() {

        return heartbeat;
    }
}

package com.zekri.yassine.wear.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.zekri.yassine.linked.Constants;

/**
 * Created by Zekri on 11/02/2018.
 */

public class SynchronizeAsyncTaskHeart extends AsyncTask<Integer, Integer, Integer> {


    private DataClient mDataClient;

    public SynchronizeAsyncTaskHeart(Activity mContext) {

        super();
        mDataClient = Wearable.getDataClient(mContext);
    }

    @Override
    protected Integer doInBackground(Integer... params) {

        int heartBeatCount = params[0];
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(Constants.HEART_COUNT_PATH);
        putDataMapRequest.getDataMap().putInt(Constants.HEARTBEAT_COUNT_VALUE, heartBeatCount);
        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        putDataRequest.setUrgent();
        Task<DataItem> putDataTask = mDataClient.putDataItem(putDataRequest);

        return -1;
    }
}



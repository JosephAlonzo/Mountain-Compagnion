package com.zekri.yassine.wear;

import android.support.annotation.NonNull;

/**
 * Created by Zekri on 11/02/2018.
 */

public class SessionId {

    public String sessionId;

    public <T extends SessionId> T widthId(@NonNull final String id) {

        this.sessionId = id;
        return (T) this;
    }
}

package com.pg.pgguardkiki.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by zzj on 16-7-25.
 */
public abstract class BaseService extends Service {
    private static final String ClassName = "LoginActivity";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(ClassName, ClassName + " onCreate() ");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(ClassName,ClassName + " onBind() ");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(ClassName,ClassName + " onUnbind() ");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i(ClassName, ClassName + " onRebind() ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(ClassName, ClassName + " onDestroy() ");
    }
}

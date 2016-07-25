package com.pg.pgguardkiki.service;

import android.os.Binder;
import android.os.IBinder;

import  com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
/**
 * Created by zzj on 16-7-25.
 */
public class ConnectService extends BaseService{
    private IConnectionStatusChangedCallback mConnectionStatusChangedCallback;
    private IBinder mBinder = new CSBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public class CSBinder extends Binder {
        public ConnectService getService() {
            return ConnectService.this;
        }
    }

    public void registerConnectionStatusCallback(IConnectionStatusChangedCallback ICSCC) {
        mConnectionStatusChangedCallback = ICSCC;
    }

    public void unRegisterConnectionStatusCallback() {
        mConnectionStatusChangedCallback = null;
    }
}

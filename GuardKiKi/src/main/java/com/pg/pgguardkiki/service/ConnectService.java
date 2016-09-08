package com.pg.pgguardkiki.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import  com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
import com.pg.pgguardkiki.tools.SmackManagerTool;

/**
 * Created by zzj on 16-7-25.
 */
public class ConnectService extends BaseService{
    private static final String ClassName = "ConnectService";
    private IConnectionStatusChangedCallback mConnectionStatusChangedCallback;
    private IBinder mBinder = new CSBinder();
    private Thread mConnectThread;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(ClassName, "onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
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

    public void Login(final String phone,final String password) {
        Log.d(ClassName, "Login() phone:" + phone+"password:"+password);
        mConnectThread = new Thread() {
            @Override
            public void run() {
                SmackManagerTool smt = new SmackManagerTool(ConnectService.this);
                smt.login(phone,password);
            }
        };
        mConnectThread.start();
    }

    public void postConnectionFailed(final String reason) {
        Log.d(ClassName, "postConnectionFailed");
    }

    // 收到新消息
    public void newMessage(final String from, final String message) {
        Log.d(ClassName, "newMessage from:"+from+"message:"+message);
    }
}

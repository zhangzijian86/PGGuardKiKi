package com.pg.pgguardkiki.service;

import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import  com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
import com.pg.pgguardkiki.tools.SmackManagerTool;

/**
 * Created by zzj on 16-7-25.
 */
public class ConnectService extends BaseService{
    public static final int CONNECTED = 0;
    public static final int DISCONNECTED = -1;
    public static final int CONNECTING = 1;
    public static final int Verify = 100;
    public static final int Register = 200;
    public static final int ChangePassword = 300;
    private int mConnectedState = DISCONNECTED; // 是否已经连接
    // 自动重连 start
    private static final int RECONNECT_AFTER = 5;
    private int mReconnectTimeout = RECONNECT_AFTER;
    public static final String LOGIN_FAILED = "Login Failed";// 登录失败
    private static final String ClassName = "ConnectService";
    private IConnectionStatusChangedCallback mConnectionStatusChangedCallback;
    private IBinder mBinder = new CSBinder();
    SmackManagerTool smt;
    private Thread mConnectThread;
    private Handler mMainHandler = new Handler();

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

    // 是否连接上服务器
    public boolean isAuthenticated() {
        if (smt != null) {
            return smt.isAuthenticated();
        }

        return false;
    }

    public void Login(final String phone,final String password) {
        Log.d(ClassName, "Login() phone:" + phone+"password:"+password);
        if (mConnectThread != null) {
            Log.d(ClassName, "a connection is still goign on!");
            return;
        }
        mConnectThread = new Thread() {
            @Override
            public void run() {
                try {
                    postConnecting();
                    smt = new SmackManagerTool(ConnectService.this);
                    if (smt.login(phone,password)) {
                        // 登陆成功
                        postConnectionScuessed();
                    } else {
                        // 登陆失败
                        postConnectionFailed(LOGIN_FAILED);
                    }
                } catch (Exception e) {
                    String message = e.getLocalizedMessage();
                    // 登陆失败
                    if (e.getCause() != null)
                        message += "\n" + e.getCause().getLocalizedMessage();
                    postConnectionFailed(message);
                    e.printStackTrace();
                } finally {
                    if (mConnectThread != null)
                        synchronized (mConnectThread) {
                            mConnectThread = null;
                        }
                }
            }
        };
        mConnectThread.start();
    }

    // 连接中，通知界面线程做一些处理
    private void postConnecting() {
        // TODO Auto-generated method stub
        mMainHandler.post(new Runnable() {
            public void run() {
                connecting();
            }
        });
    }

    private void connecting() {
        // TODO Auto-generated method stub
        mConnectedState = CONNECTING;// 连接中
        if (mConnectionStatusChangedCallback != null)
            mConnectionStatusChangedCallback.connectionStatusChanged(mConnectedState,
                    "");
    }

    private void postConnectionScuessed() {
        mMainHandler.post(new Runnable() {
            public void run() {
                connectionScuessed();
            }
        });
    }


    public void postConnectionFailed(final String reason) {
        Log.d(ClassName, "postConnectionFailed");
        mConnectedState = DISCONNECTED;// 更新当前连接状态
        mReconnectTimeout = RECONNECT_AFTER;// 重置重连的时间

        if (mConnectionStatusChangedCallback != null)
            mConnectionStatusChangedCallback.connectionStatusChanged(mConnectedState,
                    "");
    }

    private void connectionScuessed() {
        mConnectedState = CONNECTED;// 已经连接上
        mReconnectTimeout = RECONNECT_AFTER;// 重置重连的时间

        if (mConnectionStatusChangedCallback != null)
            mConnectionStatusChangedCallback.connectionStatusChanged(mConnectedState,
                    "");
    }

    // 发送消息
    public void getVerifyNumber(final String phonenumber,final String activityType) {
        mConnectThread = new Thread() {
            @Override
            public void run() {
                smt = new SmackManagerTool(ConnectService.this);
                if(smt.getVerifyNumber(phonenumber,activityType)){
                    mConnectionStatusChangedCallback.connectionStatusChanged(CONNECTED,"验证码已发送");
                }else{
                    mConnectionStatusChangedCallback.connectionStatusChanged(DISCONNECTED,"网络链接失败,请重试！");
                }
            }
        };
        mConnectThread.start();
//        else
//            smt.sendOfflineMessage(getContentResolver(), user, message);
    }

    // 发送消息
    public void sendMessage(String user, String message) {
        if (smt != null)
            smt.sendMessage(user, message);
//        else
//            smt.sendOfflineMessage(getContentResolver(), user, message);
    }

    public void register(final String  name, final String password) {
        Log.d(ClassName, "register=a=000==");
        mConnectThread = new Thread() {
            @Override
            public void run() {
                Log.d(ClassName, "register=a=111==");
                smt = new SmackManagerTool(ConnectService.this);
                Log.d(ClassName, "register=a=222==");
                smt.register(name, password);
                Log.d(ClassName, "register=a=333==");
            }
        };
        mConnectThread.start();
    }

    public void getRoster() {
        Log.d(ClassName, "getRoster=a=000==");
        mConnectThread = new Thread() {
            @Override
            public void run() {
                Log.d(ClassName, "getRoster=a=111==");
                smt = new SmackManagerTool(ConnectService.this);
                Log.d(ClassName, "getRoster=a=222==");
                smt.getRoster();
                Log.d(ClassName, "getRoster=a=333==");
            }
        };
        mConnectThread.start();
    }

    public void changePassword(final String  phone, final String password) {
        Log.d(ClassName, "register=a=000==");
        mConnectThread = new Thread() {
            @Override
            public void run() {
                Log.d(ClassName, "register=a=111==");
                smt = new SmackManagerTool(ConnectService.this);
                Log.d(ClassName, "register=a=222==");
                smt.changePassword(phone, password);
                Log.d(ClassName, "register=a=333==");
            }
        };
        mConnectThread.start();
    }

    // 收到新消息
    public void newMessage(final String from, final String message) {
        Log.d(ClassName, "newMessage from:"+from+"message:"+message);
    }

    // 收到新消息
    public void getVerifyNumberSuccess(final String from, final String message) {
        Log.d(ClassName, "newMessage from:" + from + "message:" + message);
        String content = message.replace("Verify:","");
        mConnectionStatusChangedCallback.connectionStatusChanged(Verify, content);
    }

    // 收到新消息
    public void isChangePasswordSuccess(final String from, final String message) {
        Log.d(ClassName, "newMessage from:" + from + "message:" + message);
        String content = message.replace("Forget:","");
        mConnectionStatusChangedCallback.connectionStatusChanged(ChangePassword, content);
    }

    // 收到新消息
    public void changePasswordSuccess(final String from, final String message) {
        Log.d(ClassName, "newMessage from:" + from + "message:" + message);
        String content = message.replace("ChangePassword:","");
        mConnectionStatusChangedCallback.connectionStatusChanged(ChangePassword, content);
    }


    // 收到新消息
    public void registerSuccess(final String from, final String message) {
        mConnectionStatusChangedCallback.connectionStatusChanged(Register,message);
    }

    // 收到新消息
    public void connectError(String type) {
        //Log.d(ClassName, "newMessage from:"+from+"message:"+message);
    }
}

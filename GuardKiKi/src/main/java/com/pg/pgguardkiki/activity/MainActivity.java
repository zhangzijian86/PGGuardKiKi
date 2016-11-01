package com.pg.pgguardkiki.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
import com.pg.pgguardkiki.service.ConnectService;
import com.pg.pgguardkiki.tools.ActivityCollector;
import com.pg.pgguardkiki.tools.MyToast;
import com.pg.pgguardkiki.tools.view.RoundImageView;
import com.pg.pgguardkiki.tools.view.ShapeLoadingDialog;

public class MainActivity extends Activity implements
        IConnectionStatusChangedCallback, View.OnClickListener{
    private static final String ClassName = "MainActivity";
    private static final int MAIN_OUT_TIME = 0;
    public static final String MAIN_ACTION = "COM.PG.PGGUARDKIKI.ACTIVITY.ACTION.MAIN";
    private ConnectionOutTimeProcess mMainOutTimeProcess;
    private ConnectService mMainConnectService;
    private ShapeLoadingDialog mMainDialog;
    private long firstTime;
    private LinearLayout leftBarLL;
    private RelativeLayout transparentRL;
    private TranslateAnimation mHiddenAction;
    private TranslateAnimation mShowAction;
    private RoundImageView mainlogoRI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mServiceIntent = new Intent(this, ConnectService.class);
        mServiceIntent.setAction(MAIN_ACTION);

        bindService(mServiceIntent, mMainServiceConnection,
                Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);

        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,-1.0f ,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        mHiddenAction.setDuration(500);

        mainlogoRI  = (RoundImageView)findViewById(R.id.mainlogoRI);
        mainlogoRI.setOnClickListener(this);
        leftBarLL = (LinearLayout)findViewById(R.id.leftBarLL);
        leftBarLL.setOnClickListener(this);
        transparentRL = (RelativeLayout)findViewById(R.id.transparentRL);
        transparentRL.setOnClickListener(this);

        mMainDialog=new ShapeLoadingDialog(this);
        mMainDialog.setLoadingText("数据获取中...");

        mMainOutTimeProcess = new ConnectionOutTimeProcess();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ClassName, "====Main====onDestroy========");
        try {
            unbindService(mMainServiceConnection);
        } catch (IllegalArgumentException e) {
        }
        if (mMainOutTimeProcess != null) {
            mMainOutTimeProcess.stop();
            mMainOutTimeProcess = null;
        }
    }

    @Override
    public void connectionStatusChanged(int connectedState, String content) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainlogoRI:
                leftBarLL.startAnimation(mShowAction);
                leftBarLL.setVisibility(View.VISIBLE);
                //mMainConnectService.sendMessage("specialfrienduser@zzj/Spark","mainlogoRI send text");
                break;
            case R.id.transparentRL:
                leftBarLL.startAnimation(mHiddenAction);
                leftBarLL.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            finish();
        } else {
            firstTime = System.currentTimeMillis();
            MyToast.showShort(MainActivity.this, "再按一次退出程序!");
        }
    }

    // 注册超时处理线程
    class ConnectionOutTimeProcess implements Runnable {
        public boolean running = false;
        private long startTime = 0L;
        private Thread thread = null;

        ConnectionOutTimeProcess() {
        }

        public void run() {
            while (true) {
                if (!this.running)
                    return;
                if (System.currentTimeMillis() - this.startTime > 20 * 1000L) {
                    mHandler.sendEmptyMessage(MAIN_OUT_TIME);
                }
                try {
                    Thread.sleep(10L);
                } catch (Exception localException) {
                }
            }
        }

        public void start() {
            try {
                this.thread = new Thread(this);
                this.running = true;
                this.startTime = System.currentTimeMillis();
                this.thread.start();
            } finally {
            }
        }

        public void stop() {
            try {
                this.running = false;
                this.thread = null;
                this.startTime = 0L;
            } finally {
            }
        }
    }

    ServiceConnection mMainServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMainConnectService = ((ConnectService.CSBinder) service).getService();
            mMainConnectService.registerConnectionStatusCallback(MainActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMainConnectService.unRegisterConnectionStatusCallback();
            mMainConnectService = null;
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MAIN_OUT_TIME:
                    if (mMainOutTimeProcess != null
                            && mMainOutTimeProcess.running)
                        mMainOutTimeProcess.stop();
                    if (mMainDialog != null && mMainDialog.isShowing())
                        mMainDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "网络链接失败！", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

    };
}

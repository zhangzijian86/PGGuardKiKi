package com.pg.pgguardkiki.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.fagment.FindFagment;
import com.pg.pgguardkiki.fagment.HomeFagment;
import com.pg.pgguardkiki.fagment.ProfileFagment;
import com.pg.pgguardkiki.fagment.SearchFagment;
import com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
import com.pg.pgguardkiki.service.ConnectService;
import com.pg.pgguardkiki.tools.ActivityCollector;
import com.pg.pgguardkiki.tools.MyToast;
import com.pg.pgguardkiki.tools.view.RoundImageView;
import com.pg.pgguardkiki.tools.view.ShapeLoadingDialog;

public class MainActivity extends FragmentActivity implements
        IConnectionStatusChangedCallback, View.OnClickListener {
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

    //-------
    private FrameLayout mHomeContent;
    private RadioGroup mHomeRadioGroup;
    private RadioButton mHomeHomeRb;
    private RadioButton mHomeFindRb;
    private RadioButton mHomeSearchRb;
    private RadioButton mHomeProfileRb;
    static final int NUM_ITEMS = 4;//一共四个fragment
    private HomeFagment mHomeFagment;
    private FindFagment mFindFagment;
    private SearchFagment mSearchFagment;
    private ProfileFagment mProfileFagment;
    private Fragment fragment = null;
    //-------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mServiceIntent = new Intent(this, ConnectService.class);
        mServiceIntent.setAction(MAIN_ACTION);

        bindService(mServiceIntent, mMainServiceConnection,
                Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);

        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        mHiddenAction.setDuration(500);

        mainlogoRI = (RoundImageView) findViewById(R.id.mainlogoRI);
        mainlogoRI.setOnClickListener(this);
        leftBarLL = (LinearLayout) findViewById(R.id.leftBarLL);
        leftBarLL.setOnClickListener(this);
        transparentRL = (RelativeLayout) findViewById(R.id.transparentRL);
        transparentRL.setOnClickListener(this);

        mMainDialog = new ShapeLoadingDialog(this);
        mMainDialog.setLoadingText("数据获取中...");

        mMainOutTimeProcess = new ConnectionOutTimeProcess();

        //-------
        mHomeFagment = new HomeFagment();
        mFindFagment = new FindFagment();
        mSearchFagment = new SearchFagment();
        mProfileFagment = new ProfileFagment();

        mHomeContent = (FrameLayout) findViewById(R.id.mHomeContent); //tab上方的区域
        mHomeRadioGroup = (RadioGroup) findViewById(R.id.mHomeRadioGroup);  //底部的四个tab
        mHomeHomeRb = (RadioButton) findViewById(R.id.mHomeHomeRb);
        mHomeFindRb = (RadioButton) findViewById(R.id.mHomeFindRb);
        mHomeSearchRb = (RadioButton) findViewById(R.id.mHomeSearchRb);
        mHomeProfileRb = (RadioButton) findViewById(R.id.mHomeProfileRb);
        //监听事件：为底部的RadioGroup绑定状态改变的监听事件
        mHomeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = 0;
                switch (checkedId) {
                    case R.id.mHomeHomeRb:
                        index = 0;
                        break;
                    case R.id.mHomeFindRb:
                        index = 1;
                        break;
                    case R.id.mHomeSearchRb:
                        index = 2;
                        break;
                    case R.id.mHomeProfileRb:
                        index = 3;
                        break;
                }
                //通过fragments这个adapter还有index来替换帧布局中的内容
                fragment = (Fragment) fragments.instantiateItem(mHomeContent, index);
                //一开始将帧布局中 的内容设置为第一个
                fragments.setPrimaryItem(mHomeContent, 0, fragment);
                fragments.finishUpdate(mHomeContent);

            }
        });
        //通过fragments这个adapter还有index来替换帧布局中的内容
        fragment = (Fragment) fragments.instantiateItem(mHomeContent, 0);
        //一开始将帧布局中 的内容设置为第一个
        fragments.setPrimaryItem(mHomeContent, 0, fragment);
        fragments.finishUpdate(mHomeContent);
        //-------
    }

    //-------
    //用adapter来管理四个Fragment界面的变化。注意，我这里用的Fragment都是v4包里面的
    FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

        @Override
        public int getCount() {
            return NUM_ITEMS;//一共有四个Fragment
        }

        //进行Fragment的初始化
        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0://首页
                    fragment = mHomeFagment;
                    break;
                case 1://发现
                    fragment = mFindFagment;
                    break;

                case 2://搜索
                    fragment = mSearchFagment;
                    break;

                case 3://我的
                    fragment = mProfileFagment;
                    break;
                default:
                    new HomeFagment();
                    break;
            }
            return fragment;
        }
    };
    //-------

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
//                leftBarLL.startAnimation(mShowAction);
//                leftBarLL.setVisibility(View.VISIBLE);
                //mMainConnectService.sendMessage("specialfrienduser@zzj/Spark","mainlogoRI send text");
                mHomeFagment.setTextColor();
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

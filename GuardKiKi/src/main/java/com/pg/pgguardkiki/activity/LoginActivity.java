package com.pg.pgguardkiki.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
import com.pg.pgguardkiki.service.ConnectService;
import com.pg.pgguardkiki.tools.ActivityCollector;
import com.pg.pgguardkiki.tools.MyToast;
import com.pg.pgguardkiki.tools.view.ShapeLoadingDialog;

/**
 * Created by zzj on 16-7-25.
 */
public class LoginActivity extends Activity implements
        IConnectionStatusChangedCallback , View.OnClickListener {
    private static final int LOGIN_OUT_TIME = 0;
    private static final String ClassName = "LoginActivity";
    public static final String LOGIN_ACTION = "COM.PG.PGGUARDKIKI.ACTIVITY.ACTION.LOGIN";
    private ConnectService mLoginConnectService;
    private Button mLoginBt;
    private EditText mPhoneEdit;
    private EditText mPasswordEdit;
    private TextView mForgetTV;
    private TextView mRegisterTV;
    private TextView mPhoneT;
    private TextView mPasswordT;

    private ShapeLoadingDialog mLoginDialog;
    private ConnectionOutTimeProcess mLoginOutTimeProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        Intent mServiceIntent = new Intent(this, ConnectService.class);
        mServiceIntent.setAction(LOGIN_ACTION);

        bindService(mServiceIntent, mLoginServiceConnection,
                Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);

        mLoginBt = (Button)findViewById(R.id.loginBt);
        mLoginBt.setOnClickListener(this);

        mForgetTV =  (TextView)findViewById(R.id.forgetTV);
        mForgetTV.setOnClickListener(this);

        mRegisterTV =  (TextView)findViewById(R.id.registerTV);
        mRegisterTV.setOnClickListener(this);

        mPhoneEdit = (EditText)findViewById(R.id.phoneET);
        mPhoneEdit.addTextChangedListener(textWatcher);

        mPasswordEdit = (EditText)findViewById(R.id.passwordET);
        mPasswordEdit.addTextChangedListener(textWatcher);

        mPhoneT =  (TextView)findViewById(R.id.phoneT);
        mPasswordT =  (TextView)findViewById(R.id.passwordT);

        mLoginDialog=new ShapeLoadingDialog(this);
        mLoginDialog.setLoadingText("登陆中...");

        mLoginOutTimeProcess = new ConnectionOutTimeProcess();

        ((ActivityCollector) getApplication()).addActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.loginBt:
                login();
                break;
            case  R.id.registerTV:
                Log.d(ClassName, "registerTV() ==00==");
                Intent intentRegister = new Intent();
                intentRegister.setClass(LoginActivity.this, RegisterActivity.class);
                intentRegister.putExtra("ActivityType", "Register");
                startActivity(intentRegister);
                  //register
//                mConnectService.register("zzj3", "123456");
//                Log.d(ClassName, "register() ==11==");
                  //register
//                mConnectService.sendMessage("zzz","TTT");

//                mConnectService.yazhengma("zzz","TTT");

//                mConnectService.sendMessage(mPhoneEdit.getText().toString().trim(),
//                        mPasswordEdit.getText().toString().trim());

//                Intent intent = new Intent(LoginActivity.this,MapDemo.class);
//                startActivity(intent);
                break;
            case  R.id.forgetTV:
                Log.d(ClassName, "forgetTV() ==00==");
                Intent intentChangePassword = new Intent();
                intentChangePassword.setClass(LoginActivity.this, RegisterActivity.class);
                intentChangePassword.putExtra("ActivityType", "ChangePassword");
                startActivity(intentChangePassword);
                break;
            default:
                break;
        }
    }

    public void login(){
        Log.d(ClassName, "login() ==00==");
        if (TextUtils.isEmpty(mPhoneEdit.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), R.string.login_phoneempty, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(ClassName, "login() ==11==");
        if (TextUtils.isEmpty(mPasswordEdit.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), R.string.login_passwdempty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mLoginConnectService != null) {
            Log.d(ClassName, "login() ==22==");
            if (mLoginDialog != null && !mLoginDialog.isShowing())
                mLoginDialog.show();
            if (mLoginOutTimeProcess != null && !mLoginOutTimeProcess.running)
                mLoginOutTimeProcess.start();
            mLoginConnectService.Login(mPhoneEdit.getText().toString().trim(), mPasswordEdit.getText().toString().trim());
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(mPhoneEdit.getText().toString().trim().equals("")){
                mPhoneT.setVisibility(View.VISIBLE);
            }else{
                mPhoneT.setVisibility(View.INVISIBLE);
            }
            if(mPasswordEdit.getText().toString().equals("")){
                mPasswordT.setVisibility(View.VISIBLE);
            }else{
                mPasswordT.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void connectionStatusChanged(int connectedState, String content) {
        if (mLoginDialog != null && mLoginDialog.isShowing())
            mLoginDialog.dismiss();
        if (mLoginOutTimeProcess != null && mLoginOutTimeProcess.running) {
            mLoginOutTimeProcess.stop();
            mLoginOutTimeProcess = null;
        }
        if (connectedState == mLoginConnectService.CONNECTED) {
            Log.d(ClassName, "==connectionStatusChanged=CONNECTED=" + content);
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            intent.putExtra("PhoneNumber", mPhoneEdit.getText().toString().trim());
            intent.putExtra("Password", mPasswordEdit.getText().toString().trim());
            startActivity(intent);
            finish();
        } else if (connectedState == mLoginConnectService.DISCONNECTED) {
            Looper.prepare();
            MyToast.showShort(LoginActivity.this, "网络链接失败，请重试!");
            Looper.loop();
            Log.d(ClassName, "==connectionStatusChanged=DISCONNECTED=" + content);
        }
    }

    ServiceConnection mLoginServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLoginConnectService = ((ConnectService.CSBinder) service).getService();
            mLoginConnectService.registerConnectionStatusCallback(LoginActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLoginConnectService.unRegisterConnectionStatusCallback();
            mLoginConnectService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ClassName, "====Login====onDestroy========");
        try {
            unbindService(mLoginServiceConnection);
        } catch (IllegalArgumentException e) {
        }
        if (mLoginOutTimeProcess != null) {
            mLoginOutTimeProcess.stop();
            mLoginOutTimeProcess = null;
        }
        ((ActivityCollector) getApplication()).removeActivity(this);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_OUT_TIME:
                    if (mLoginOutTimeProcess != null
                            && mLoginOutTimeProcess.running)
                        mLoginOutTimeProcess.stop();
                    if (mLoginDialog != null && mLoginDialog.isShowing())
                        mLoginDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "网络链接失败！", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

    };

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
                    mHandler.sendEmptyMessage(LOGIN_OUT_TIME);
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
}

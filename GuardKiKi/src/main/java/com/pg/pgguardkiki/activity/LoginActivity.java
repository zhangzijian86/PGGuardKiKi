package com.pg.pgguardkiki.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import com.pg.pgguardkiki.tools.view.ShapeLoadingDialog;

/**
 * Created by zzj on 16-7-25.
 */
public class LoginActivity extends Activity implements
        IConnectionStatusChangedCallback , View.OnClickListener {
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

    private ShapeLoadingDialog shapeLoadingDialog;

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

        shapeLoadingDialog=new ShapeLoadingDialog(this);
        shapeLoadingDialog.setLoadingText("加载中...");

        ((ActivityCollector) getApplication()).addActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.loginBt:
                shapeLoadingDialog.show();
                //login();
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
    public void connectionStatusChanged(int connectedState, String reason) {

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
//        if (mRegisterOutTimeProcess != null) {
//            mRegisterOutTimeProcess.stop();
//            mRegisterOutTimeProcess = null;
//        }
        ((ActivityCollector) getApplication()).removeActivity(this);
    }
}
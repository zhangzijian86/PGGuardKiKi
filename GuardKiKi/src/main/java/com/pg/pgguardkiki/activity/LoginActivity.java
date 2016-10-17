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

/**
 * Created by zzj on 16-7-25.
 */
public class LoginActivity extends Activity implements
        IConnectionStatusChangedCallback, TextWatcher , View.OnClickListener {
    private static final String ClassName = "LoginActivity";
    public static final String LOGIN_ACTION = "COM.PG.PGGUARDKIKI.ACTIVITY.ACTION.LOGIN";
    private ConnectService mConnectService;
    private Button loginBt;
    private EditText mPhoneEdit;
    private EditText mPasswordEdit;
    private TextView forgetTV;
    private TextView registerTV;
    private TextView phoneT;
    private TextView passwordT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        Intent mServiceIntent = new Intent(this, ConnectService.class);
        mServiceIntent.setAction(LOGIN_ACTION);

        bindService(mServiceIntent, mServiceConnection,
                Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);

        loginBt = (Button)findViewById(R.id.loginBt);
        loginBt.setOnClickListener(this);

        forgetTV =  (TextView)findViewById(R.id.forgetTV);
        forgetTV.setOnClickListener(this);

        registerTV =  (TextView)findViewById(R.id.registerTV);
        registerTV.setOnClickListener(this);

        mPhoneEdit = (EditText)findViewById(R.id.phoneET);
        mPhoneEdit.addTextChangedListener(textWatcher);

        mPasswordEdit = (EditText)findViewById(R.id.passwordET);
        mPasswordEdit.addTextChangedListener(textWatcher);

        phoneT =  (TextView)findViewById(R.id.phoneT);
        passwordT =  (TextView)findViewById(R.id.passwordT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.loginBt:
                login();
                break;
            case  R.id.registerTV:
                Log.d(ClassName, "registerTV() ==00==");
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
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
        if (mConnectService != null) {
            Log.d(ClassName, "login() ==22==");
            mConnectService.Login(mPhoneEdit.getText().toString().trim(), mPasswordEdit.getText().toString().trim());
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(mPhoneEdit.getText().toString().equals("")){
                phoneT.setVisibility(View.VISIBLE);
            }else{
                phoneT.setVisibility(View.INVISIBLE);
            }
            if(mPasswordEdit.getText().toString().equals("")){
                passwordT.setVisibility(View.VISIBLE);
            }else{
                passwordT.setVisibility(View.INVISIBLE);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mConnectService = ((ConnectService.CSBinder) service).getService();
            mConnectService.registerConnectionStatusCallback(LoginActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mConnectService.unRegisterConnectionStatusCallback();
            mConnectService = null;
        }
    };
}
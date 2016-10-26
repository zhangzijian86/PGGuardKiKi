package com.pg.pgguardkiki.activity;

import android.app.Activity;
import android.app.Dialog;
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
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
import com.pg.pgguardkiki.service.ConnectService;
import com.pg.pgguardkiki.tools.ActivityCollector;
import com.pg.pgguardkiki.tools.MyToast;

public class RegisterVerifyActivity extends Activity implements
		IConnectionStatusChangedCallback , View.OnClickListener{
	private static final String ClassName = "RegisterVerifyActivity";
	public static final String REGISTER_VERIFY_ACTION = "COM.PG.PGGUARDKIKI.ACTIVITY.ACTION.REGISTER";

	private static final int LOGIN_OUT_TIME = 0;

	private ConnectService mRegisterVerifyConnectService;
	private ConnectionOutTimeProcess mRegisterVerifyOutTimeProcess;

	private Dialog mRegisterDialog;

	private Button mRegisterverifyBt;
	private EditText mRegisterverifynumberET;
	private TextView mRegisterverifynumberT;
	private String mPhoneNumber;
	private String mVerifyNumber;
	private RelativeLayout mRegisterpasswordRL;
	private RelativeLayout mRegisterconfirmpasswordRL;

	private EditText mRegisterpasswordET;
	private TextView mRegisterpasswordT;

	private EditText mRegisterconfirmpasswordET;
	private TextView mRegisterconfirmpasswordT;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_registerverify);

		Intent mServiceIntent = new Intent(this, ConnectService.class);
		mServiceIntent.setAction(REGISTER_VERIFY_ACTION);

		bindService(mServiceIntent, mRegisterVerifyConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);

		mVerifyNumber = this.getIntent().getStringExtra("verifyNumber");
		Log.d(ClassName, "==onCreate==" + mVerifyNumber);
		mPhoneNumber = this.getIntent().getStringExtra("phoneNumber");
		Log.d(ClassName, "==onCreate==" + mPhoneNumber);

		mRegisterverifyBt = (Button)findViewById(R.id.registerverifyBt);
		mRegisterverifyBt.setOnClickListener(this);

		mRegisterverifynumberET = (EditText)findViewById(R.id.registerverifynumberET);
		mRegisterverifynumberET.addTextChangedListener(textWatcher);

		mRegisterverifynumberT = (TextView)findViewById(R.id.registerverifynumberT);

		mRegisterpasswordET  = (EditText)findViewById(R.id.registerpasswordET);
		mRegisterpasswordET.addTextChangedListener(textWatcher);

		mRegisterpasswordT = (TextView)findViewById(R.id.registerpasswordT);

		mRegisterconfirmpasswordET  = (EditText)findViewById(R.id.registerconfirmpasswordET);
		mRegisterconfirmpasswordET.addTextChangedListener(textWatcher);

		mRegisterconfirmpasswordT = (TextView)findViewById(R.id.registerconfirmpasswordT);

		mRegisterpasswordRL  = (RelativeLayout)findViewById(R.id.registerpasswordRL);
		mRegisterpasswordRL.setVisibility(View.INVISIBLE);
		mRegisterconfirmpasswordRL  = (RelativeLayout)findViewById(R.id.registerconfirmpasswordRL);
		mRegisterconfirmpasswordRL.setVisibility(View.INVISIBLE);

		mRegisterDialog = getRegisterDialog(this);

		mRegisterVerifyOutTimeProcess = new ConnectionOutTimeProcess();

		((ActivityCollector) getApplication()).addActivity(this);
	}

	public Dialog getRegisterDialog(Activity context) {

		final Dialog dialog = new Dialog(context, R.style.DialogStyle);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.activity_dialog);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		int screenW = getScreenWidth(context);
		lp.width = (int) (0.6 * screenW);

		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
		titleTxtv.setText(R.string.register_getverifynumber);
		return dialog;
	}

	public static int getScreenWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case LOGIN_OUT_TIME:
					if (mRegisterVerifyOutTimeProcess != null
							&& mRegisterVerifyOutTimeProcess.running)
						mRegisterVerifyOutTimeProcess.stop();
					if (mRegisterDialog != null && mRegisterDialog.isShowing())
						mRegisterDialog.dismiss();
					Toast.makeText(getApplicationContext(), "网络链接失败22222！", Toast.LENGTH_SHORT).show();
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

	ServiceConnection mRegisterVerifyConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mRegisterVerifyConnectService = ((ConnectService.CSBinder) service).getService();
			mRegisterVerifyConnectService.registerConnectionStatusCallback(RegisterVerifyActivity.this);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mRegisterVerifyConnectService.unRegisterConnectionStatusCallback();
			mRegisterVerifyConnectService = null;
		}
	};

	@Override
	public void connectionStatusChanged(int connectedState, String content) {
		// TODO Auto-generated method stub
		if (mRegisterDialog != null && mRegisterDialog.isShowing())
			mRegisterDialog.dismiss();
		if (mRegisterVerifyOutTimeProcess != null && mRegisterVerifyOutTimeProcess.running) {
			mRegisterVerifyOutTimeProcess.stop();
			mRegisterVerifyOutTimeProcess = null;
		}
		if (connectedState == mRegisterVerifyConnectService.CONNECTED) {
			Log.d(ClassName, "==connectionStatusChanged=CONNECTED=" + content);
//			Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();

		} else if (connectedState == mRegisterVerifyConnectService.DISCONNECTED) {
			Looper.prepare();
			MyToast.showShort(RegisterVerifyActivity.this, "网络链接失败，请重试!");
			Looper.loop();
			Log.d(ClassName, "==connectionStatusChanged=DISCONNECTED=" + content);
//			Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
		}

		if(connectedState == mRegisterVerifyConnectService.Register){
			if(content.equals("RegisterSuccess")){
				Looper.prepare();
				MyToast.showShort(RegisterVerifyActivity.this, "注册成功!");
				Log.d(ClassName, "==connectionStatusChanged=RegisterSuccess=" + content);
				((ActivityCollector) getApplication()).finishAll();
				Looper.loop();
			}else{

			}
		}
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			if (!mRegisterverifynumberET.getText().toString().trim().equals("")
				&&mRegisterverifynumberET.getText().toString().trim().equals(mVerifyNumber)) {
				mRegisterpasswordRL.setVisibility(View.VISIBLE);
				mRegisterconfirmpasswordRL.setVisibility(View.VISIBLE);
				mRegisterverifynumberET.setEnabled(false);

				RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams)mRegisterverifyBt.getLayoutParams();
				textParams.addRule(RelativeLayout.BELOW, mRegisterconfirmpasswordRL.getId());
				mRegisterverifyBt.setLayoutParams(textParams);
			}

			if(mRegisterverifynumberET.getText().toString().trim().equals("")){
				mRegisterverifynumberT.setVisibility(View.VISIBLE);
			}else{
				mRegisterverifynumberT.setVisibility(View.INVISIBLE);
			}

			if(mRegisterpasswordET.getText().toString().trim().equals("")){
				mRegisterpasswordT.setVisibility(View.VISIBLE);
			}else{
				mRegisterpasswordT.setVisibility(View.INVISIBLE);
			}

			if(mRegisterconfirmpasswordET.getText().toString().trim().equals("")){
				mRegisterconfirmpasswordT.setVisibility(View.VISIBLE);
			}else{
				mRegisterconfirmpasswordT.setVisibility(View.INVISIBLE);
			}

			if(!mRegisterpasswordET.getText().toString().trim().equals("")&&
			   !mRegisterconfirmpasswordET.getText().toString().trim().equals("")
				&&mRegisterpasswordET.getText().toString().trim().equals(mRegisterconfirmpasswordET.getText().toString().trim())){
				mRegisterverifyBt.setEnabled(true);
			}else{
				mRegisterverifyBt.setEnabled(false);
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
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.registerverifyBt:
				if(mRegisterpasswordET.getText().toString().length()<8){
					Toast.makeText(getApplicationContext(), "密码长度应大于或等于8位！", Toast.LENGTH_SHORT).show();
					Log.d(ClassName, "==RegisterVerify=no=ok=" + mRegisterverifyBt.isEnabled());
				}else{
					Log.d(ClassName, "==RegisterVerify=ok="+mRegisterverifyBt.isEnabled());
					if (mRegisterDialog != null && !mRegisterDialog.isShowing())
						mRegisterDialog.show();
					if (mRegisterVerifyOutTimeProcess != null && !mRegisterVerifyOutTimeProcess.running)
						mRegisterVerifyOutTimeProcess.start();
					if (mRegisterVerifyConnectService != null) {
						mRegisterVerifyConnectService.register(mPhoneNumber,mRegisterpasswordET.getText().toString());
					}
				}
				break;
			default:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(ClassName, "====RegisterVerify====onDestroy========");
		try {
			unbindService(mRegisterVerifyConnection);
		} catch (IllegalArgumentException e) {
		}
		if (mRegisterVerifyOutTimeProcess != null) {
			mRegisterVerifyOutTimeProcess.stop();
			mRegisterVerifyOutTimeProcess = null;
		}
		((ActivityCollector) getApplication()).removeActivity(this);
	}
}

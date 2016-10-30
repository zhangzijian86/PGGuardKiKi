package com.pg.pgguardkiki.activity;

import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
import com.pg.pgguardkiki.service.ConnectService;
import com.pg.pgguardkiki.tools.ActivityCollector;
import com.pg.pgguardkiki.tools.MyToast;
import com.pg.pgguardkiki.tools.view.ShapeLoadingDialog;

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
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements
		IConnectionStatusChangedCallback , View.OnClickListener{
	private static final int LOGIN_OUT_TIME = 0;
	private static final String ClassName = "RegisterActivity";
	private ConnectService mRegisterConnectService;
	private ConnectionOutTimeProcess mRegisterOutTimeProcess;
	public static final String REGISTER_ACTION = "COM.PG.PGGUARDKIKI.ACTIVITY.ACTION.REGISTER";
	private Button mRegisterBt;
	private EditText mRegisterphoneET;
	private TextView mRegisterphoneT;
	private ShapeLoadingDialog mRegisterDialog;
	private String mActivityType;

	ServiceConnection mRegisterConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mRegisterConnectService = ((ConnectService.CSBinder) service).getService();
			mRegisterConnectService.registerConnectionStatusCallback(RegisterActivity.this);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mRegisterConnectService.unRegisterConnectionStatusCallback();
			mRegisterConnectService = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		mActivityType  = this.getIntent().getStringExtra("ActivityType");
		Log.d(ClassName, "==onCreate==" + mActivityType);

		Intent mServiceIntent = new Intent(this, ConnectService.class);
		mServiceIntent.setAction(REGISTER_ACTION);

		bindService(mServiceIntent, mRegisterConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);

		mRegisterBt = (Button)findViewById(R.id.registerBt);
		mRegisterBt.setOnClickListener(this);

		mRegisterphoneET = (EditText)findViewById(R.id.registerphoneET);
		mRegisterphoneET.addTextChangedListener(textWatcher);

		mRegisterphoneT = (TextView)findViewById(R.id.registerphoneT);

		mRegisterDialog = new ShapeLoadingDialog(this);
		mRegisterDialog.setLoadingText("加载中...");//getRegisterDialog(this);

		mRegisterOutTimeProcess = new ConnectionOutTimeProcess();

		((ActivityCollector) getApplication()).addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(ClassName, "====Register====onDestroy========");
		try {
			unbindService(mRegisterConnection);
		} catch (IllegalArgumentException e) {
		}
		if (mRegisterOutTimeProcess != null) {
			mRegisterOutTimeProcess.stop();
			mRegisterOutTimeProcess = null;
		}
		((ActivityCollector) getApplication()).removeActivity(this);
	}

//	public Dialog getRegisterDialog(Activity context) {
//
//		final Dialog dialog = new Dialog(context, R.style.DialogStyle);
//		dialog.setCancelable(false);
//		dialog.setContentView(R.layout.activity_dialog);
//		Window window = dialog.getWindow();
//		WindowManager.LayoutParams lp = window.getAttributes();
//
//		int screenW = getScreenWidth(context);
//		lp.width = (int) (0.6 * screenW);
//
//		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
//		titleTxtv.setText(R.string.register_getverifynumber);
//		return dialog;
//	}

	public static int getScreenWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			if (mRegisterphoneET.getText().toString().trim().equals("")) {
				mRegisterBt.setEnabled(false);
				mRegisterphoneT.setVisibility(View.VISIBLE);
			} else {
				mRegisterBt.setEnabled(true);
				mRegisterphoneT.setVisibility(View.INVISIBLE);
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
			case R.id.registerBt:
				Log.d(ClassName, "==registerBt==" + mRegisterBt.isEnabled());
				if (mRegisterDialog != null && !mRegisterDialog.isShowing())
					mRegisterDialog.show();
				if (mRegisterOutTimeProcess != null && !mRegisterOutTimeProcess.running)
					mRegisterOutTimeProcess.start();
				if (mRegisterConnectService != null) {
					mRegisterConnectService.getVerifyNumber(mRegisterphoneET.getText().toString().trim(),mActivityType);
				}
				break;
			default:
				break;
		}
	}

	// 收到新消息
	public void getVerifyNumberSuccess(final String from, final String message) {
		Log.d(ClassName, "newMessage from:"+from+"message:"+message);
	}

	@Override
	public void connectionStatusChanged(int connectedState, String content) {
		// TODO Auto-generated method stub
		if (mRegisterDialog != null && mRegisterDialog.isShowing())
			mRegisterDialog.dismiss();
		if (mRegisterOutTimeProcess != null && mRegisterOutTimeProcess.running) {
			mRegisterOutTimeProcess.stop();
			mRegisterOutTimeProcess = null;
		}
		if (connectedState == mRegisterConnectService.CONNECTED) {
			Log.d(ClassName, "==connectionStatusChanged=CONNECTED=" + content);
//			Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();

		} else if (connectedState == mRegisterConnectService.DISCONNECTED) {
			Looper.prepare();
			MyToast.showShort(RegisterActivity.this, "网络链接失败，请重试!");
			Looper.loop();
			Log.d(ClassName, "==connectionStatusChanged=DISCONNECTED=" + content);
//			Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
		}

		if(connectedState == mRegisterConnectService.Verify){
			if(content.startsWith("HasRegistered")){
				Looper.prepare();
				MyToast.showShort(RegisterActivity.this, "此手机号码已注册!");
				Looper.loop();
				Log.d(ClassName, "==connectionStatusChanged=HasRegistered=111=" + content);
//				Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
			}else{
				Log.d(ClassName, "==connectionStatusChanged=Unregistered=222=" + content);
				content = content.replace("Unregistered:","");
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, RegisterVerifyActivity.class);
				intent.putExtra("ActivityType", mActivityType);
				intent.putExtra("phoneNumber", mRegisterphoneET.getText().toString().trim());
				intent.putExtra("verifyNumber", content);
				startActivity(intent);
			}
		}

		if(connectedState == mRegisterConnectService.ChangePassword){
			if(content.startsWith("HasRegistered")){
				Log.d(ClassName, "==connectionStatusChanged=Unregistered=111=" + content);
				content = content.replace("HasRegistered:","");
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, RegisterVerifyActivity.class);
				intent.putExtra("ActivityType", mActivityType);
				intent.putExtra("phoneNumber", mRegisterphoneET.getText().toString().trim());
				intent.putExtra("verifyNumber", content);
				startActivity(intent);
			}else{
				Looper.prepare();
				MyToast.showShort(RegisterActivity.this, "此手机号码未注册!");
				Looper.loop();
				Log.d(ClassName, "==connectionStatusChanged=HasRegistered=222=" + content);
//				Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case LOGIN_OUT_TIME:
					if (mRegisterOutTimeProcess != null
							&& mRegisterOutTimeProcess.running)
						mRegisterOutTimeProcess.stop();
					if (mRegisterDialog != null && mRegisterDialog.isShowing())
						mRegisterDialog.dismiss();
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

package com.pg.pgguardkiki.activity;

import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
import com.pg.pgguardkiki.service.ConnectService;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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

public class RegisterActivity extends Activity implements
		IConnectionStatusChangedCallback , View.OnClickListener{
	private static final String ClassName = "RegisterActivity";
	private ConnectService mRegisterConnectService;
	public static final String REGISTER_ACTION = "COM.PG.PGGUARDKIKI.ACTIVITY.ACTION.REGISTER";
	private Button mRegisterBt;
	private EditText mRegisterphoneET;
	private TextView mRegisterphoneT;
	private Dialog mRegisterDialog;

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

		Intent mServiceIntent = new Intent(this, ConnectService.class);
		mServiceIntent.setAction(REGISTER_ACTION);

		bindService(mServiceIntent, mRegisterConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);

		mRegisterBt = (Button)findViewById(R.id.registerBt);
		mRegisterBt.setOnClickListener(this);

		mRegisterphoneET = (EditText)findViewById(R.id.registerphoneET);
		mRegisterphoneET.addTextChangedListener(textWatcher);

		mRegisterphoneT = (TextView)findViewById(R.id.registerphoneT);

		mRegisterDialog = getRegisterDialog(this);
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

	@Override
	public void connectionStatusChanged(int connectedState, String reason) {

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
				if (mRegisterConnectService != null) {
					mRegisterConnectService.yazhengma("zzz","TTT");
				}
//				Intent intent = new Intent();
//		    	intent.setClass(RegisterActivity.this, RegisterVerifyActivity.class);
//				intent.putExtra("phoneNumber", mRegisterphoneET.getText().toString().trim());
//		    	intent.putExtra("verifyNumber", "123456");
//		    	startActivity(intent);
				break;
			default:
				break;
		}
	}
}

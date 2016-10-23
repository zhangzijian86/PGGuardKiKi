package com.pg.pgguardkiki.activity;

import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
import com.pg.pgguardkiki.service.ConnectService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity implements
		IConnectionStatusChangedCallback , View.OnClickListener{
	private static final String ClassName = "RegisterActivity";
	private ConnectService mConnectService;
	public static final String REGISTER_ACTION = "COM.PG.PGGUARDKIKI.ACTIVITY.ACTION.REGISTER";
	private Button registerBt;
	private EditText registerphoneET;
	private TextView registerphoneT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		registerBt = (Button)findViewById(R.id.registerBt);
		registerBt.setOnClickListener(this);

		registerphoneET = (EditText)findViewById(R.id.registerphoneET);
		registerphoneET.addTextChangedListener(textWatcher);

		registerphoneT = (TextView)findViewById(R.id.registerphoneT);

	}

	@Override
	public void connectionStatusChanged(int connectedState, String reason) {

	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			if (registerphoneET.getText().toString().trim().equals("")) {
				registerBt.setEnabled(false);
				registerphoneT.setVisibility(View.VISIBLE);
			} else {
				registerBt.setEnabled(true);
				registerphoneT.setVisibility(View.INVISIBLE);
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
				Log.d(ClassName, "==registerBt==" + registerBt.isEnabled());
				Intent intent = new Intent();
		    	intent.setClass(RegisterActivity.this, RegisterVerifyActivity.class);
		    	intent.putExtra("verifyNumber", "123456");
		    	startActivity(intent);
				break;
			default:
				break;
		}
	}
}

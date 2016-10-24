package com.pg.pgguardkiki.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;

public class RegisterVerifyActivity extends Activity implements
		IConnectionStatusChangedCallback , View.OnClickListener{
	private static final String ClassName = "RegisterVerifyActivity";
	public static final String REGISTER_ACTION = "COM.PG.PGGUARDKIKI.ACTIVITY.ACTION.REGISTER";
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

	}

	@Override
	public void connectionStatusChanged(int connectedState, String reason) {

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
				}
				break;
			default:
				break;
		}
	}
}

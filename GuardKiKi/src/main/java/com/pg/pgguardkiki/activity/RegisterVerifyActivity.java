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
	private Button registerverifyBt;
	private EditText registerverifynumberET;
	private TextView registerverifynumberT;
	private String verifyNumber;
	private RelativeLayout registerpasswordRL;
	private RelativeLayout registerconfirmpasswordRL;

	private EditText registerpasswordET;
	private TextView registerpasswordT;

	private EditText registerconfirmpasswordET;
	private TextView registerconfirmpasswordT;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_registerverify);

		verifyNumber = this.getIntent().getStringExtra("verifyNumber");
		Log.d(ClassName, "==onCreate==" + verifyNumber);

		registerverifyBt = (Button)findViewById(R.id.registerverifyBt);
		registerverifyBt.setOnClickListener(this);

		registerverifynumberET = (EditText)findViewById(R.id.registerverifynumberET);
		registerverifynumberET.addTextChangedListener(textWatcher);

		registerverifynumberT = (TextView)findViewById(R.id.registerverifynumberT);

		registerpasswordET  = (EditText)findViewById(R.id.registerpasswordET);
		registerpasswordET.addTextChangedListener(textWatcher);

		registerpasswordT = (TextView)findViewById(R.id.registerpasswordT);

		registerconfirmpasswordET  = (EditText)findViewById(R.id.registerconfirmpasswordET);
		registerconfirmpasswordET.addTextChangedListener(textWatcher);

		registerconfirmpasswordT = (TextView)findViewById(R.id.registerconfirmpasswordT);

		registerpasswordRL  = (RelativeLayout)findViewById(R.id.registerpasswordRL);
		registerpasswordRL.setVisibility(View.INVISIBLE);
		registerconfirmpasswordRL  = (RelativeLayout)findViewById(R.id.registerconfirmpasswordRL);
		registerconfirmpasswordRL.setVisibility(View.INVISIBLE);

	}

	@Override
	public void connectionStatusChanged(int connectedState, String reason) {

	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			if (!registerverifynumberET.getText().toString().trim().equals("")
				&&registerverifynumberET.getText().toString().trim().equals(verifyNumber)) {
				registerpasswordRL.setVisibility(View.VISIBLE);
				registerconfirmpasswordRL.setVisibility(View.VISIBLE);
				registerverifynumberET.setEnabled(false);

				RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams)registerverifyBt.getLayoutParams();
				textParams.addRule(RelativeLayout.BELOW, registerconfirmpasswordRL.getId());
				registerverifyBt.setLayoutParams(textParams);
			}

			if(registerverifynumberET.getText().toString().trim().equals("")){
				registerverifynumberT.setVisibility(View.VISIBLE);
			}else{
				registerverifynumberT.setVisibility(View.INVISIBLE);
			}

			if(registerpasswordET.getText().toString().trim().equals("")){
				registerpasswordT.setVisibility(View.VISIBLE);
			}else{
				registerpasswordT.setVisibility(View.INVISIBLE);
			}

			if(registerconfirmpasswordET.getText().toString().trim().equals("")){
				registerconfirmpasswordT.setVisibility(View.VISIBLE);
			}else{
				registerconfirmpasswordT.setVisibility(View.INVISIBLE);
			}

			if(!registerpasswordET.getText().toString().trim().equals("")&&
			   !registerconfirmpasswordET.getText().toString().trim().equals("")
				&&registerpasswordET.getText().toString().trim().equals(registerconfirmpasswordET.getText().toString().trim())){
				registerverifyBt.setEnabled(true);
			}else{
				registerverifyBt.setEnabled(false);
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
				if(registerpasswordET.getText().toString().length()<8){
					Toast.makeText(getApplicationContext(), "密码长度应大于或等于8位！", Toast.LENGTH_SHORT).show();
					Log.d(ClassName, "==RegisterVerify=no=ok=" + registerverifyBt.isEnabled());
				}else{
					Log.d(ClassName, "==RegisterVerify=ok="+registerverifyBt.isEnabled());
				}
				break;
			default:
				break;
		}
	}
}

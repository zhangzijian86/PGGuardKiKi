package com.pg.pgguardkiki.activity;

import java.util.Timer;
import java.util.TimerTask;
import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.interfaces.IConnectionStatusChangedCallback;
import com.pg.pgguardkiki.service.ConnectService;
import com.pg.pgguardkiki.tools.LoadingProgressDialog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements
		IConnectionStatusChangedCallback {
	private ConnectService mConnectService;
	public static final String REGISTER_ACTION = "COM.PG.PGGUARDKIKI.ACTIVITY.ACTION.REGISTER";
	private Button btqueding;
	private Button yanzhengmaBtn;
	private EditText shoujihaoma;
	private EditText yanzhengma;
	private ImageView tu;
	private LoadingProgressDialog dialog;
	private String yanzhengmaReturn;
	private Timer timer;
	private int miao;
	private String type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		Intent mServiceIntent = new Intent(this, ConnectService.class);
		mServiceIntent.setAction(REGISTER_ACTION);
		bindService(mServiceIntent, mServiceConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
		
	   Intent intent = getIntent(); //用于激活它的意图对象        
	   type = intent.getStringExtra("type");
		
	   Log.d("==RegisterActivity===", "====type===="+type);
	   
		btqueding = (Button) findViewById(R.id.queding);
		yanzhengmaBtn = (Button) findViewById(R.id.yanzhengmaBtn);
		btqueding.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String phoneNumber = shoujihaoma.getText().toString().trim();
				String yanZhengMa = yanzhengma.getText().toString().trim();
		    	if(phoneNumber.equals("")){
		    		Toast.makeText(getApplicationContext(), "请填写手机号码！", Toast.LENGTH_SHORT).show();
		    	}else{
		    		if(phoneNumber.length()!=11){
		    			Toast.makeText(getApplicationContext(), "请正确填写11位手机号码！", Toast.LENGTH_SHORT).show();
		    		}else{
		    			if(isNumeric(phoneNumber)){
		    				if(!TextUtils.isEmpty(phoneNumber)&&!TextUtils.isEmpty(yanZhengMa)){
		    					if(yanzhengmaReturn.equals(yanZhengMa)){
		    						Toast.makeText(getApplicationContext(), "登陆成功！", Toast.LENGTH_SHORT).show();

		    			          if(type!=null&&type.equals("yijianyuyue")){
		    			        	    Log.d("==RegisterActivity===", "====yijianyuyue===="+phoneNumber);
		    			        	    type = "yijianyuyuefinish";
		    			          }else{
		    			            	finish();
		    			            }
		    					}else{
		    						Toast.makeText(getApplicationContext(), "验证码错误！", Toast.LENGTH_SHORT).show();
		    					}
		    				}else{
		    					Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
		    				}	
		    			}else{
		    				Toast.makeText(getApplicationContext(), "手机号码应为数字！", Toast.LENGTH_SHORT).show();
		    			}
		    		}	    		
		    	}
			}
		});
		yanzhengmaBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String phoneNumber = shoujihaoma.getText().toString().trim();
		    	if(phoneNumber.equals("")){
		    		Toast.makeText(getApplicationContext(), "请填写手机号码！", Toast.LENGTH_SHORT).show();
		    	}else{
		    		if(phoneNumber.length()!=11){
		    			Toast.makeText(getApplicationContext(), "请正确填写11位手机号码！", Toast.LENGTH_SHORT).show();
		    		}else{
		    			if(isNumeric(phoneNumber)){
							mConnectService.yazhengma("zzz","TTT");
							//timer.schedule(task,1, 1000);
		    			}else{
		    				Toast.makeText(getApplicationContext(), "手机号码应为数字！", Toast.LENGTH_SHORT).show();
		    			}
		    		}	    		
		    	}				
			}
		});
		
		shoujihaoma = (EditText) findViewById(R.id.shoujihaoma);
		yanzhengma = (EditText) findViewById(R.id.yanzhengma);
		tu = (ImageView) findViewById(R.id.tu);
		tu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		//初始化dialog
		dialog=new LoadingProgressDialog(this,"正在加载...");
		//初始化dialog end
		miao = 60;
		timer = new Timer(true);
	}
	
	public boolean isNumeric(String str){
		for (int i = str.length() ; --i>=0 ; ){   
			if (!Character.isDigit(str.charAt ( i ) ) ){
				return false;
			}
		}
		return true;
	}
	
	final Handler handler = new Handler(){  
		public void handleMessage(Message msg) {  
			switch (msg.what) {      
			case 1:      
				miao = miao - 1;
				if(miao>=0){
					yanzhengmaBtn.setText("    "+miao+"    ");
					yanzhengmaBtn.setEnabled(false);
				}else{
					yanzhengmaBtn.setText("验证码");
					yanzhengmaBtn.setEnabled(true);
					timer.cancel();
					miao = 60;
				}
				break;      
			}      
			super.handleMessage(msg);  
		}    
	};
	
	TimerTask task = new TimerTask(){  
		public void run() {  
			Message message = new Message();      
			message.what = 1;
			handler.sendMessage(message);    
		}  
	};

	ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mConnectService = ((ConnectService.CSBinder) service).getService();
			mConnectService.registerConnectionStatusCallback(RegisterActivity.this);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mConnectService.unRegisterConnectionStatusCallback();
			mConnectService = null;
		}
	};

	@Override
	public void connectionStatusChanged(int connectedState, String reason) {

	}
}

package com.pg.pgguardkiki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.pg.pgguardkiki.R;
import com.pg.pgguardkiki.dao.PGDBHelperFactory;
import com.pg.pgguardkiki.tools.ConstantTool;
import com.pg.pgguardkiki.tools.SystemDataTool;

/**
 * Created by zzj on 16-7-22.
 */
public class SplashActivity extends AppCompatActivity {
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();

        String password = SystemDataTool.getString(this,
                ConstantTool.PASSWORD, "");
		if (!TextUtils.isEmpty(password)) {
			mHandler.postDelayed(toMainActivity, 3000);
		} else {
            mHandler.postDelayed(toLoginActivity, 3000);
		}
        PGDBHelperFactory.getDBHelper();
    }

    Runnable toLoginActivity = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    };

    Runnable toMainActivity = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };
}
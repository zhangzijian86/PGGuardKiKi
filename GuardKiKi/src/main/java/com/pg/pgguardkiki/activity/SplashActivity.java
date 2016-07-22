package com.pg.pgguardkiki.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.pg.pgguardkiki.R;

/**
 * Created by zzj on 16-7-22.
 */
public class SplashActivity extends AppCompatActivity {
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String password = PreferenceUtils.getPrefString(this,
//                PreferenceConstants.PASSWORD, "");
////		if (!TextUtils.isEmpty(password)) {
////			mHandler.postDelayed(gotoMainAct, 3000);
////		} else {
//        mHandler.postDelayed(gotoLoginAct, 3000);
////		}
    }
}
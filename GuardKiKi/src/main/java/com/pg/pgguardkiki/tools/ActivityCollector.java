package com.pg.pgguardkiki.tools;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzj on 10/26/16.
 */
public class ActivityCollector  extends Application {

    private static final String ClassName = "ActivityCollector";

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            Log.d(ClassName, "====RegisterVerify====onDestroy========" + activity.getLocalClassName());
            if(activity.getLocalClassName().indexOf("LoginActivity")==-1){
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
    }
}

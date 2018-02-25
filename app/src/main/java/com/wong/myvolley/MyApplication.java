package com.wong.myvolley;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by wong on 18-2-24.
 */

public class MyApplication extends Application {
    private static RequestQueue queue;
    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(getApplicationContext());
        CrashReport.initCrashReport(getApplicationContext(), "488fda5776", false);

    }
    public static RequestQueue getHttpQueue(){
        return queue;
    }
}

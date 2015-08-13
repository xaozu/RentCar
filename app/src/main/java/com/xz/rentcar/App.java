package com.xz.rentcar;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by xaozu on 15/8/4.
 * 全局控制
 */
public class App extends Application{
    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        // 全局上下文
        sContext = getApplicationContext();
        try {
            SDKInitializer.initialize(this);
        } catch (Exception e) {
        }

    }




}

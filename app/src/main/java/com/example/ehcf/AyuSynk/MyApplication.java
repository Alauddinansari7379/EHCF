package com.example.ehcf.AyuSynk;

import android.app.Application;

import com.ayudevice.ayusynksdk.AyuSynk;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AyuSynk.init(getApplicationContext());

    }
}

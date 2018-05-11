package com.svk.tutorialapplication;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Administrator on 05-05-2018.
 */

public class TutorialApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}

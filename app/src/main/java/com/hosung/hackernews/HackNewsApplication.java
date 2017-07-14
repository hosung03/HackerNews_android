package com.hosung.hackernews;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Hosung, Lee on 2017. 7. 10..
 */

public class HackNewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}

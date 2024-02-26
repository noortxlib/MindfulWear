package com.example.mindfulwear;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class MindfulWear extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialise ThreeTenABP
        AndroidThreeTen.init(this);
    }
}
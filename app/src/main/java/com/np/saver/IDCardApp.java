package com.np.saver;

import android.app.Application;

import com.fxn.stash.Stash;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.FirebaseDatabase;
import com.np.saver.Utils.Constants;
import com.onesignal.OneSignal;

public class IDCardApp extends Application {

    private static final String ONESIGNAL_APP_ID = "f8d214e4-dcf0-4f97-b7e1-d48b5ae9013f";

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.context = this;
        //Add this line in ApplicationContext.java
        Stash.init(this);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, initializationStatus -> {
        });

    }
}
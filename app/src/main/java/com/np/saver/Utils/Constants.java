package com.np.saver.Utils;

import android.content.Context;
import android.provider.Settings;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {
    public static final String CATEGORIES = "CATEGORIES";
    public static final String PARAMS = "PARAMS";
    public static final String NAME = "NAME";
    public static final String CURRENT_CARD = "CURRENT_CARD";
    public static final String CARDS = "CARDS";
    public static final String CURRENT_POSITION = "CURRENT_POSITION";
    public static final String IS_EDIT = "IS_EDIT";
    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String BACK = "BACK";
    public static final String IMG_CLICKED = "IMG_CLICKED";
    public static final String FRONT = "FRONT";

    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static Context context;

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child("IdCardApp")
                .child(Stash.getString(DEVICE_ID, Constants.getDeviceID()));
        db.keepSynced(true);
        return db;
    }

    public static String getDeviceID() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}

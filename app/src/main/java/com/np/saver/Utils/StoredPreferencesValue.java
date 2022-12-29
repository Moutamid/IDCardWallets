package com.np.saver.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StoredPreferencesValue {
    public static final String APP_LAUNCH_FIRST_TIME = "FIRST_TIME_LAUNCH";
    public static final String APP_PASSWORD = "APP_PASSWORD";
    public static final String EMAIL_ID = "EMAIL_ID";
    public static final String EMAIL_SERVICE = "EMAIL_SERVICE";
    static final String IS_FIRST_LUNCH = "IS_FIRST_LUNCH";
    public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    static final String MyPref = "userPref";
    public static final String SMS_SERVICE = "SMS_SERVICE";

    public static void setIsAppFirstTimeLaunch(String str, Boolean bool, Context context) {
        SharedPreferences.Editor wcontext = PreferenceManager.getDefaultSharedPreferences(context).edit();
        wcontext.putBoolean(str, bool.booleanValue());
        wcontext.commit();
    }

    public static boolean getIsAppFirstTimeLaunch(String str, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(str, true);
    }

    public static void setSecurityQuesAns(Context context, String str) {
        SharedPreferences.Editor econtext = context.getSharedPreferences("sq", 0).edit();
        econtext.putString("ans", str);
        econtext.apply();
    }

    public static String getSecurityQuesAns(Context context) {
        return context.getSharedPreferences("sq", 0).getString("ans", "0");
    }

    public static int getSecurityQue(Context context) {
        return context.getSharedPreferences("sq", 0).getInt("que", 0);
    }

    public static void setSecurityQue(Context context, int i) {
        SharedPreferences.Editor rcontext = context.getSharedPreferences("sq", 0).edit();
        rcontext.putInt("que", i);
        rcontext.apply();
    }

    public static void setPassword(String str, String str2, Context context) {
        SharedPreferences.Editor tcontext = PreferenceManager.getDefaultSharedPreferences(context).edit();
        tcontext.putString(str, str2);
        tcontext.commit();
    }

    public static boolean isFirstLaunch(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_FIRST_LUNCH, true);
    }

    public static void setFirstLaunch(Context context, boolean z) {
        SharedPreferences.Editor qcontext = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        qcontext.putBoolean(IS_FIRST_LUNCH, z);
        qcontext.commit();
    }

    public static String getPassword(String str, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(str, "");
    }

    public static String getMobile(String str, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(str, "+919999999999");
    }

    public static String getEmail(String str, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(str, "abc@example.com");
    }

    public static Boolean getSMSService(String str, Context context) {
        return Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(str, false));
    }

    public static Boolean getEmailService(String str, Context context) {
        return Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(str, true));
    }
}

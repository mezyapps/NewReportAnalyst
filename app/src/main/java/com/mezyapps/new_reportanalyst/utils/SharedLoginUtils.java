package com.mezyapps.new_reportanalyst.utils;

import android.content.Context;
import android.content.SharedPreferences;



import java.util.ArrayList;


public class SharedLoginUtils {


    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static String getLoginSharedUtils(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        return preferences.getString(ConstantFields.IS_LOGIN, "");
    }

    public static void putLoginSharedUtils(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(ConstantFields.IS_LOGIN, "true");
        editor.commit();
    }

    public static void removeLoginSharedUtils(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(ConstantFields.IS_LOGIN, "false");
        editor.commit();
    }





}

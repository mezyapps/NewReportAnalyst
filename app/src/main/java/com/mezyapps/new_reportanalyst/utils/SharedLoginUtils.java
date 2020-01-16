package com.mezyapps.new_reportanalyst.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.mezyapps.new_reportanalyst.model.UserProfileModel;

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

    public static void addUserProfile(Context mContext, ArrayList<UserProfileModel> userProfileModelArrayList) {

        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(ConstantFields.USER_ID,userProfileModelArrayList.get(0).getUser_id());
        editor.putString(ConstantFields.USER_NAME, userProfileModelArrayList.get(0).getUser_name());
        editor.putString(ConstantFields.USER_PASS, userProfileModelArrayList.get(0).getUser_pass());
        editor.putString(ConstantFields.DB_NAME, userProfileModelArrayList.get(0).getDb_name());
        editor.putString(ConstantFields.DB_USER_NAME, userProfileModelArrayList.get(0).getUser_name());
        editor.putString(ConstantFields.DB_USER_PASS, userProfileModelArrayList.get(0).getUser_pass());
        editor.putString(ConstantFields.USER_TYPE, userProfileModelArrayList.get(0).getUser_type());
        editor.putString(ConstantFields.SALESMAN_ID, userProfileModelArrayList.get(0).getSALESMAN_ID());

        editor.commit();
    }

    public static ArrayList<UserProfileModel> getUserProfile(Context mContext) {
        ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
        UserProfileModel userProfileModel = new UserProfileModel();
        preferences = mContext.getSharedPreferences(ConstantFields.LOGIN_PREFERENCE, mContext.MODE_PRIVATE);
        userProfileModel.setUser_id(preferences.getString(ConstantFields.USER_ID, ""));
        userProfileModel.setUser_name(preferences.getString(ConstantFields.USER_NAME, ""));
        userProfileModel.setUser_pass(preferences.getString(ConstantFields.USER_PASS, ""));
        userProfileModel.setDb_name(preferences.getString(ConstantFields.DB_NAME, ""));
        userProfileModel.setUser_name(preferences.getString(ConstantFields.DB_USER_NAME, ""));
        userProfileModel.setUser_pass(preferences.getString(ConstantFields.DB_USER_PASS, ""));
        userProfileModel.setUser_type(preferences.getString(ConstantFields.USER_TYPE, ""));
        userProfileModel.setSALESMAN_ID(preferences.getString(ConstantFields.SALESMAN_ID, ""));
        userProfileModelArrayList.add(userProfileModel);
        return userProfileModelArrayList;
    }
}

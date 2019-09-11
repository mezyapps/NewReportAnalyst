package com.mezyapps.new_reportanalyst.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mezyapps.new_reportanalyst.model.DatabaseConfigModel;

import java.util.ArrayList;

public class DatabaseConfiguration {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static String getDatabaseConfiguration(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.DATABASE_PREFERENCE, mContext.MODE_PRIVATE);
        return preferences.getString(ConstantFields.IS_CONFIG, "");
    }
    public static void putDatabaseConfiguration(Context mContext) {
        preferences = mContext.getSharedPreferences(ConstantFields.DATABASE_PREFERENCE, mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(ConstantFields.IS_CONFIG, "true");
        editor.commit();
    }
    public static void addDatabaseConfig(Context mContext, ArrayList<DatabaseConfigModel> databaseConfigModelArrayList) {

        preferences = mContext.getSharedPreferences(ConstantFields.DATABASE_PREFERENCE, mContext.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(ConstantFields.IP_ADDRESS,databaseConfigModelArrayList.get(0).getIp_address());
        editor.putString(ConstantFields.DATABASE_NAME, databaseConfigModelArrayList.get(0).getDatabase());
        editor.putString(ConstantFields.USERNAME, databaseConfigModelArrayList.get(0).getUsername());
        editor.putString(ConstantFields.PASSWORD, databaseConfigModelArrayList.get(0).getPassword());
        editor.commit();
    }

    public static ArrayList<DatabaseConfigModel> getDatabaseConfig(Context mContext) {
        ArrayList<DatabaseConfigModel> databaseConfigModelArrayList = new ArrayList<>();
        DatabaseConfigModel databaseConfigModel = new DatabaseConfigModel();
        preferences = mContext.getSharedPreferences(ConstantFields.DATABASE_PREFERENCE, mContext.MODE_PRIVATE);
        databaseConfigModel.setIp_address(preferences.getString(ConstantFields.IP_ADDRESS, ""));
        databaseConfigModel.setDatabase(preferences.getString(ConstantFields.DATABASE_NAME, ""));
        databaseConfigModel.setUsername(preferences.getString(ConstantFields.USERNAME, ""));
        databaseConfigModel.setPassword(preferences.getString(ConstantFields.PASSWORD, ""));
        databaseConfigModelArrayList.add(databaseConfigModel);
        return databaseConfigModelArrayList;
    }
}

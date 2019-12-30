package com.mezyapps.new_reportanalyst.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, DatabaseConstant.DATABASE_NAME, null, DatabaseConstant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstant.ProductMaster.CREATE_PRODUCT_TABLE);
        db.execSQL(DatabaseConstant.GroupPer.CREATE_GROUPS_PER_TABLE);
        db.execSQL(DatabaseConstant.SalesTable.CREATE_SALES_TABLE);
        db.execSQL(DatabaseConstant.SalesDetails.CREATE_SALES_DETAILS);
        db.execSQL(DatabaseConstant.PurchaseTableHD.CREATE_PURCHASE_TABLE);
        db.execSQL(DatabaseConstant.PurchaseDetails.CREATE_PURCHASE_DETAILS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.ProductMaster.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.GroupPer.GROUP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.SalesTable.SALES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.SalesDetails.SALES_DETAILS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.PurchaseTableHD.PURCHASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.PurchaseDetails.PURCHASE_DETAILS_TABLE);
        onCreate(db);
    }
}

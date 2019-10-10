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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.ProductMaster.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.GroupPer.GROUP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.SalesTable.SALES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.SalesDetails.SALES_DETAILS_TABLE);
        // Create tables again
        onCreate(db);
    }
    public void deleteAllTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("delete from " + DatabaseConstant.ProductMaster.TABLE_NAME);
            db.execSQL("delete from " + DatabaseConstant.GroupPer.GROUP_TABLE);
            db.execSQL("delete from " + DatabaseConstant.SalesTable.SALES_TABLE);
            db.execSQL("delete from " + DatabaseConstant.SalesDetails.SALES_DETAILS_TABLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        db.close();
    }

    public boolean insertTable(ResultSet resultSet,String TableName) throws SQLException {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        while(resultSet.next()) {
            int columnCount = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                String columnValue = resultSet.getString(i);
                contentValues.put(columnName, columnValue);
            }
        }

        /* Inserting Row */
        long result = db.insert(TableName, null, contentValues);
        if (result == -1) {
            db.close(); // Closing database connection
            return false;
        } else {
            db.close(); // Closing database connection
            return true;
        }
    }

}

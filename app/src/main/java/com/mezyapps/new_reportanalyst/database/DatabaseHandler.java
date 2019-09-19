package com.mezyapps.new_reportanalyst.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, DatabaseConstant.DATABASE_NAME, null, DatabaseConstant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstant.ProductMaster.CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.ProductMaster.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public boolean insertProductTable(int product_id, String product_code, String product_name, String category_name, String product_pkg, String packing, String unit_name, String cost_price, String sale_rate, String mrp, String hsn, String gst) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues productValues = new ContentValues();
        productValues.put(DatabaseConstant.ProductMaster.PRODID, product_id);
        productValues.put(DatabaseConstant.ProductMaster.PMSTCODE, product_code);
        productValues.put(DatabaseConstant.ProductMaster.PMSTNAME, product_name);
        productValues.put(DatabaseConstant.ProductMaster.CATEGORYNAME, category_name);
        productValues.put(DatabaseConstant.ProductMaster.PRODPKGINCASE, product_pkg);
        productValues.put(DatabaseConstant.ProductMaster.PACKING, packing);
        productValues.put(DatabaseConstant.ProductMaster.UNITNAME, unit_name);
        productValues.put(DatabaseConstant.ProductMaster.COSTPRICE, cost_price);
        productValues.put(DatabaseConstant.ProductMaster.SALERATE, sale_rate);
        productValues.put(DatabaseConstant.ProductMaster.MRP, mrp);
        productValues.put(DatabaseConstant.ProductMaster.HSNCODE, hsn);
        productValues.put(DatabaseConstant.ProductMaster.GST, gst);

        // Inserting Row
        long result = db.insert(DatabaseConstant.ProductMaster.TABLE_NAME, null, productValues);
        if (result == -1) {
            db.close(); // Closing database connection
            return false;
        } else {
            db.close(); // Closing database connection
            return true;
        }
    }
}

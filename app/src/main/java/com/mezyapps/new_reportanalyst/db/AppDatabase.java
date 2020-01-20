package com.mezyapps.new_reportanalyst.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;

@Database(entities = {OrderEntryProduct.class},version = 1)
public abstract class AppDatabase  extends RoomDatabase {

    public abstract ProductOrderDAO getProductDAO();
}

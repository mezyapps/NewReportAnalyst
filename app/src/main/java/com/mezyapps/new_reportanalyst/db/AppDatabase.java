package com.mezyapps.new_reportanalyst.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductDT;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;

@Database(entities = {OrderEntryProduct.class, OrderEntryProductHD.class, OrderEntryProductDT.class},version = 2)
public abstract class AppDatabase  extends RoomDatabase {

    public abstract ProductOrderDAO getProductDAO();

    public abstract ProductOrderEntryHDDAO getProductHDDAO();

    public abstract ProductOrderEntryTDDAO getProductDTDAO();


    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {


        }
    };
}


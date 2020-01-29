package com.mezyapps.new_reportanalyst.db;

import android.app.Application;
import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.PrimaryKey;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductDT;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;
import com.mezyapps.new_reportanalyst.model.GroupPerModel;
import com.mezyapps.new_reportanalyst.model.ProductTableModel;

@Database(entities = {OrderEntryProduct.class, OrderEntryProductHD.class,
        OrderEntryProductDT.class, GroupPerModel.class, ProductTableModel.class}, version = 4)

public abstract class AppDatabase extends RoomDatabase {

    public abstract ProductOrderDAO getProductDAO();

    public abstract ProductOrderEntryHDDAO getProductHDDAO();

    public abstract ProductOrderEntryTDDAO getProductDTDAO();

    public abstract GroupPerDao getGroupPerDAO();

    public abstract  PMSTDao getPMSTDAO();


    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };

    public static AppDatabase getInStatce(Context mContext) {

         AppDatabase appDatabase = Room.databaseBuilder(mContext, AppDatabase.class, "ReportAnalyst")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
         return appDatabase;
    }

}


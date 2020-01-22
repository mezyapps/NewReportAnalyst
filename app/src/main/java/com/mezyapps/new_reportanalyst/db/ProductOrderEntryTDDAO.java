package com.mezyapps.new_reportanalyst.db;

import androidx.room.Dao;
import androidx.room.Insert;

import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductDT;

@Dao
public  interface ProductOrderEntryTDDAO {

    /*product DT*/
    @Insert
    public long insertProductDT(OrderEntryProductDT orderEntryProductDT);
}

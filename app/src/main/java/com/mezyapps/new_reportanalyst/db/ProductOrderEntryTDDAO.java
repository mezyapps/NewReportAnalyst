package com.mezyapps.new_reportanalyst.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductDT;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;

import java.util.List;

@Dao
public  interface ProductOrderEntryTDDAO {

    /*product DT*/
    @Insert
    public long insertProductDT(OrderEntryProductDT orderEntryProductDT);


    @Query("select * from orderentryproductdt where maxID ==:order_no")
    public List<OrderEntryProductDT> getOnlyIDValue(long order_no);

    @Query("DELETE FROM  orderentryproductdt where maxID==:order_no")
    public void  deleteSingleProduct(long order_no);

    @Query("DELETE FROM  orderentryproductdt")
    public void  deleteAllProduct();
}

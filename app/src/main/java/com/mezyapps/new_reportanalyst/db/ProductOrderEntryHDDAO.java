package com.mezyapps.new_reportanalyst.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProductOrderEntryHDDAO {

    @Insert
    public long insertProductHD(OrderEntryProductHD orderEntryProductHD);

    @Query("select max(maxID) from OrderEntryProductHD")
    public long getMaxValue();

    @Query("select * from OrderEntryProductHD")
    public List<OrderEntryProductHD> getAllValue();

    @Query("select * from OrderEntryProductHD where maxID ==:order_no")
    public List<OrderEntryProductHD> getOnlyIDValue(long order_no);


    @Query("DELETE FROM  OrderEntryProductHD where maxID==:order_no")
    public void  deleteSingleProduct(long order_no);
}

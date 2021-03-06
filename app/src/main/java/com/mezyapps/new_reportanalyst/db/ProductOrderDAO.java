package com.mezyapps.new_reportanalyst.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductDT;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;
import com.mezyapps.new_reportanalyst.view.activity.OrderEnteryActivity;

import java.util.List;
@Dao
public interface ProductOrderDAO {

    @Insert
    public long addProduct(OrderEntryProduct orderEntryProduct);

    @Delete
    public void  deleteProduct(OrderEntryProduct orderEntryProduct);

    @Query("DELETE FROM  orderentryproducttable")
    public void  deleteAllProduct();

    @Query("DELETE FROM  orderentryproducttable where id==:id")
    public void  deleteSingleProduct(long id);

    @Query("select * from orderentryproducttable")
    public List<OrderEntryProduct> getAppProduct();

    @Update
    public int getProductDataUpdate(OrderEntryProduct orderEntryProduct);


}

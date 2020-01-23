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

    @Query("UPDATE orderentryproducttable SET product_name = :product_name,box_pkg = :box,pkg = :pkg,qty = :qty,qty = :qty" +
            ",rate = :rate,sub_total = :sub_total,dist_per = :dist_per,gst_per = :gst_per,dist_amt= :dist_amt,gst_amt= :gst_amt" +
            ",final_total= :final_total WHERE id = :id")
    public long getProductDataUpdate(long id,String  product_name,String box,String pkg,String qty,String rate,String sub_total,
                                     String dist_per,String gst_per,String dist_amt,String gst_amt,String final_total);


}

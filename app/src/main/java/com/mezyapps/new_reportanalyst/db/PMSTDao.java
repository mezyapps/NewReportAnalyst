package com.mezyapps.new_reportanalyst.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mezyapps.new_reportanalyst.model.GroupPerModel;
import com.mezyapps.new_reportanalyst.model.ProductTableModel;

import java.util.List;

@Dao
public interface PMSTDao {
    @Insert
    public long addProduct(ProductTableModel productTableModel);


    @Query("select * from producttable")
    public List<ProductTableModel> getAllProduct();

    @Query("DELETE FROM  ProductTable")
    public void  deleteAllProdcut();
}

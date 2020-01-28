package com.mezyapps.new_reportanalyst.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;
import com.mezyapps.new_reportanalyst.model.GroupPerModel;

import java.util.List;

@Dao
public interface GroupPerDao {

    @Insert
    public long addProduct(GroupPerModel groupPerModel);


    @Query("select * from GroupPer")
    public List<GroupPerModel> getAllGroupPerTable();

    @Query("DELETE FROM  GroupPer")
    public void  deleteAllGroupPer();
}

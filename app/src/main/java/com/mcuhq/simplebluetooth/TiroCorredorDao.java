package com.mcuhq.simplebluetooth;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by eduardo.dall on 01/08/2018.
 */
@Dao
public interface TiroCorredorDao {
    @Query("SELECT * FROM tiroCorredor order by uid desc")
    List<TiroCorredor> getAll();

    @Query("SELECT * FROM tiroCorredor WHERE uid IN (:userIds)")
    List<TiroCorredor> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(TiroCorredor... users);

    @Delete
    void delete(TiroCorredor user);

    @Query("DELETE FROM tiroCorredor")
    void nukeTable();
}

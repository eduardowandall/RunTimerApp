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
    @Query("SELECT * FROM tiroCorredor")
    List<TiroCorredor> getAll();

    @Query("SELECT * FROM tiroCorredor WHERE uid IN (:userIds)")
    List<TiroCorredor> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    TiroCorredor findByName(String first, String last);

    @Insert
    void insertAll(TiroCorredor... users);

    @Delete
    void delete(TiroCorredor user);
}

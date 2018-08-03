package com.mcuhq.simplebluetooth;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by eduardo.dall on 01/08/2018.
 */

@Database(entities = {TiroCorredor.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TiroCorredorDao tiroCorredorDao();
}

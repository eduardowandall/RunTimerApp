package com.wandall.runtimer;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * Created by eduardo.dall on 01/08/2018.
 */

@Database(entities = {TiroCorredor.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TiroCorredorDao tiroCorredorDao();
}

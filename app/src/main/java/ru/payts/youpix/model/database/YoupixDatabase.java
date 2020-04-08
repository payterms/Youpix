package ru.payts.youpix.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {HitRec.class}, version = 1, exportSchema = false)
public abstract class YoupixDatabase extends RoomDatabase {
    public abstract HitDao hitDao();
}

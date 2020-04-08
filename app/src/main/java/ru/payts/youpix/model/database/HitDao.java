package ru.payts.youpix.model.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface HitDao {

    @Query("SELECT * FROM table_hits")
    Single<List<HitRec>> getAll();

    @Query("SELECT * FROM table_hits WHERE id = :id")
    Single<List<HitRec>> getAllById(int id);

    @Query("SELECT * FROM table_hits WHERE picId = :picId")
    Single<HitRec> getPictureRecordByPicId(String picId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(HitRec hit);

    @Insert
    Single<List<Long>> insertList(List<HitRec> hitRecs);

    @Delete
    Single<Integer> delete(HitRec hit);

    @Query("DELETE FROM table_hits")
    Single<Integer> deleteAll();

    @Update
    Single<Integer> update(HitRec hit);


}

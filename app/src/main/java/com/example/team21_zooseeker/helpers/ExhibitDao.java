package com.example.team21_zooseeker.helpers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExhibitDao {
    @Insert
    long insert(ExhibitEntity exhibit);

    @Insert
    List<Long> insertAll(List<ExhibitEntity> exhibits);

    @Query("SELECT * FROM `exhibit_items` WHERE `id`=:id")
    ExhibitEntity get(String id);

    @Query("SELECT * FROM `exhibit_items`")
    List<ExhibitEntity> getAll();

    @Query("SELECT * FROM `exhibit_items`")
    LiveData<List<ExhibitEntity>> getAllLive();

    @Query("SELECT * FROM `exhibit_items` WHERE `visited`=1")
    List<ExhibitEntity> getVisited();

    @Query("SELECT * FROM `exhibit_items` WHERE `visited`=0")
    List<ExhibitEntity> getUnvisited();

    @Update
    int updateVisited(ExhibitEntity exhibit);

    @Delete
    int delete(ExhibitEntity exhibit);

    @Query("DELETE FROM `exhibit_items`")
    void deleteAll();
}

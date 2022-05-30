package com.example.team21_zooseeker.helpers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExhibitEntitiesDao {
    @Insert
    void insert(ExhibitEntities exhibit);

    @Insert
    void insertAll(List<ExhibitEntities> exhibits);


    @Query("SELECT * FROM `exhibits` WHERE `dataT_ID`=:id")
    ExhibitEntities get(long id);

    @Delete
    void delete(ExhibitEntities exhibit);
}

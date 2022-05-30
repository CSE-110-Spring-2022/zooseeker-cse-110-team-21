package com.example.team21_zooseeker.helpers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {ExhibitEntity.class}, version = 1)
public abstract class ExhibitDatabase extends RoomDatabase {
    private static ExhibitDatabase singleton = null;

    public abstract ExhibitDao exhibitDao();

    public synchronized static ExhibitDatabase getSingleton(Context context) {
        if (singleton == null) {
            singleton = ExhibitDatabase.makeDatabase(context, new ArrayList<>());
        }
        return singleton;
    }

    private static ExhibitDatabase makeDatabase(Context context, List<ExhibitEntity> exhibits) {
        return Room.databaseBuilder(context, ExhibitDatabase.class, "exhibits.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            getSingleton(context).exhibitDao().insertAll(exhibits);
                        });
                    }
                })
                .build();
    }
}

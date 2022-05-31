package com.example.team21_zooseeker;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.team21_zooseeker.helpers.ExhibitDao;
import com.example.team21_zooseeker.helpers.ExhibitDatabase;
import com.example.team21_zooseeker.helpers.ExhibitEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectActivityTest {
    ExhibitDatabase testDb;
    ExhibitDao exhibitDao;

    private static void forceLayout(RecyclerView recyclerView) {
        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        recyclerView.layout(0, 0, 1080, 2280);
    }

    @Before
    public void resetDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ExhibitDatabase.class)
                .allowMainThreadQueries()
                .build();
        ExhibitDatabase.injectTestDatabase(testDb);

        ExhibitEntity exhibit1 = new ExhibitEntity("id1", "group_id1",null,"name1",
                "lat1", "lng1");
        ExhibitEntity exhibit2 = new ExhibitEntity("id2", "group_id2", null,"name2",
                "lat2", "lng2");
        ExhibitEntity exhibit3 = new ExhibitEntity("id3", "group_id3", null,"name3",
                "lat3", "lng3");
        List<ExhibitEntity> exhibits = Arrays.asList(exhibit1, exhibit2, exhibit3);
        exhibitDao = testDb.exhibitDao();
        exhibitDao.insertAll(exhibits);
    }

}

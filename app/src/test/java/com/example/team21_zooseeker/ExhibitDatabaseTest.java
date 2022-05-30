package com.example.team21_zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team21_zooseeker.helpers.ExhibitDao;
import com.example.team21_zooseeker.helpers.ExhibitDatabase;
import com.example.team21_zooseeker.helpers.ExhibitEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExhibitDatabaseTest {
    private ExhibitDao dao;
    private ExhibitDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ExhibitDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.exhibitDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsert() {
        ExhibitEntity exhibit1 = new ExhibitEntity("id1", "group_id1", null,
                "name1", "lat1", "lng1");
        ExhibitEntity exhibit2 = new ExhibitEntity("id2", "group_id2", null,
                "name2", "lat2", "lng2");

        long id1 = dao.insert(exhibit1);
        long id2 = dao.insert(exhibit2);

        // Check that these have all been inserted with unique IDs.
        assertNotEquals(id1, id2);
    }

    @Test
    public void testInsertAll() {
        ExhibitEntity exhibit1 = new ExhibitEntity("id1", "group_id1", null,
                "name1", "lat1", "lng1");
        ExhibitEntity exhibit2 = new ExhibitEntity("id2", "group_id2", null,
                "name2", "lat2", "lng2");
        ExhibitEntity exhibit3 = new ExhibitEntity("id3", "group_id3", null,
                "name3", "lat3", "lng3");

        List<ExhibitEntity> exhibitList = Arrays.asList(exhibit1, exhibit2, exhibit3);

        List<Long> dbIdList = dao.insertAll(exhibitList);

        // Check if idList is size 3
        assertEquals(3, dbIdList.size());
        assertEquals("id1", dao.get(dbIdList.get(0)).id);
    }

    @Test
    public void testGet() {
        ExhibitEntity insertedItem = new ExhibitEntity("id1", "group_id1", null,
                "name1", "lat1", "lng1");
        long dbId = dao.insert(insertedItem);

        ExhibitEntity item = dao.get(dbId);
        assertEquals(dbId, item.dbId);
        assertEquals(insertedItem.id, item.id);
        assertEquals(insertedItem.group_id, item.group_id);
        assertEquals(insertedItem.kind, item.kind);
        assertEquals(insertedItem.name, item.name);
        assertEquals(insertedItem.lat, item.lat);
        assertEquals(insertedItem.lng, item.lng);
    }

    @Test
    public void testUpdate() {
        ExhibitEntity item = new ExhibitEntity("id1", "group_id1", null,
                "name1", "lat1", "lng1");
        long id = dao.insert(item);

        item = dao.get(id);
        item.visited = true;
        int itemsUpdated = dao.updateVisited(item);
        assertEquals(1, itemsUpdated);

        item = dao.get(id);
        assertNotNull(item);
        assertEquals(true, item.visited);
    }

    @Test
    public void testDelete() {
        ExhibitEntity item = new ExhibitEntity("id1", "group_id1", null,
                "name1", "lat1", "lng1");
        long id = dao.insert(item);

        item = dao.get(id);
        int itemsDeleted = dao.delete(item);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(id));
    }
}

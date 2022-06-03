package com.example.team21_zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team21_zooseeker.activities.search_select.SearchSelectActivity;
import com.example.team21_zooseeker.helpers.ExhibitDao;
import com.example.team21_zooseeker.helpers.ExhibitDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class SelectAnimalsTest {
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

    @Rule
    public ActivityScenarioRule<SearchSelectActivity> scenarioRule = new ActivityScenarioRule<>(SearchSelectActivity.class);

    @Test
    public void exhibit_counter_test() {
        ActivityScenario<SearchSelectActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            TextView exhibitCounter = activity.findViewById(R.id.exhibit_counter);
            assertEquals("0", exhibitCounter.getText().toString());
            activity.selectedAnimals.add("Elephant Odyssey");
            activity.selectedAnimals.add("Gorillas");
            activity.selectedAnimals.add("Alligators");
            assertEquals("3", String.valueOf(activity.selectedAnimals.size()));
        });
    }

    @Test
    public void selectAnimals_test() {
        ActivityScenario<SearchSelectActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            activity.selectedAnimals.add("Elephant Odyssey");
            activity.selectedAnimals.add("Gorillas");
            activity.selectedAnimals.add("Alligators");

            assert(activity.selectedAnimals.contains("Gorillas"));
            assert(activity.selectedAnimals.contains("Alligators"));
            //assert(activity.selectedAnimals.contains("Arctic Foxes"));
        });
    }
}

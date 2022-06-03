package com.example.team21_zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team21_zooseeker.activities.directions.DirectionItem;
import com.example.team21_zooseeker.activities.directions.DirectionsActivity;
import com.example.team21_zooseeker.activities.route.Route;
import com.example.team21_zooseeker.activities.search_select.SearchSelectActivity;
import com.example.team21_zooseeker.helpers.ExhibitDao;
import com.example.team21_zooseeker.helpers.ExhibitDatabase;
import com.example.team21_zooseeker.helpers.ExhibitEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@RunWith(AndroidJUnit4.class)
public class RealTimeUpdateTests {
    // both contain ids
    private Set<String> exhibits_noGroup;
    private Set<String> exhibits_Group;

    // preset location.
    // Exhibit 1 should change to Crocodiles from orangutans
    private double lat = 32.756035;
    private double lon = -117.168192;

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
    public ActivityScenarioRule<SearchSelectActivity> scenarioRule =
            new ActivityScenarioRule<>(SearchSelectActivity.class);



    // WARNING WORKS ONLY WITH MS2 ASSETS.
    @Before
    public void setUp() {
        exhibits_noGroup = new HashSet<>();
        // exhibits with no group exhibits
        {
            exhibits_noGroup.add("capuchin");
            exhibits_noGroup.add("orangutan");
            exhibits_noGroup.add("gorilla");
        }

        exhibits_Group = new HashSet<>();
        // Contains both no group and group exhibits
        // for now only group id
        {
            exhibits_Group.add("owens_aviary");
            exhibits_Group.add("fern_canyon");
            exhibits_Group.add("gorilla");
            exhibits_Group.add("koi");
        }
    }

    @Test
    public void testLocAtExhibit() {
        ActivityScenario<SearchSelectActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity( activity -> {
            ExhibitEntity dbItem = new ExhibitEntity(activity.searchDataBase.node.get("capuchin"));
            activity.viewModel.insertExhibit(activity, dbItem);
            dbItem = new ExhibitEntity(activity.searchDataBase.node.get("orangutan"));
            activity.viewModel.insertExhibit(activity, dbItem);
            dbItem = new ExhibitEntity(activity.searchDataBase.node.get("gorilla"));
            activity.viewModel.insertExhibit(activity, dbItem);
        });

        // needed for DirectionActivity to not have a bunch of null fields
        ActivityScenario<Route> routeScenario = ActivityScenario.launch(Route.class);
        routeScenario.moveToState(Lifecycle.State.CREATED);

        ActivityScenario<DirectionsActivity> directionScenario =
                ActivityScenario.launch(DirectionsActivity.class);
        directionScenario.moveToState(Lifecycle.State.CREATED);

        directionScenario.onActivity(directionsActivity -> {

            // change our location to Capuchin,
            directionsActivity.setLocManually("capuchin");
            directionsActivity.offTrack();

            // Should be updated to say we are at Capuchin Monkeys
            ArrayList<DirectionItem> exhibit_names = directionsActivity.getbriefDirections();

            String exhibit_name = exhibit_names.get(0).getName();

            assertEquals("Capuchin Monkeys", exhibit_name);
            directionsActivity.dao.deleteAll();
        });
    }

    @Test
    public void testChangingLoc() {
        ActivityScenario<SearchSelectActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity( activity -> {
            ExhibitEntity dbItem = new ExhibitEntity(activity.searchDataBase.node.get("capuchin"));
            activity.viewModel.insertExhibit(activity, dbItem);
            dbItem = new ExhibitEntity(activity.searchDataBase.node.get("orangutan"));
            activity.viewModel.insertExhibit(activity, dbItem);
            dbItem = new ExhibitEntity(activity.searchDataBase.node.get("gorilla"));
            activity.viewModel.insertExhibit(activity, dbItem);
        });

        // needed for DirectionActivity to not have a bunch of null fields
        ActivityScenario<Route> routeScenario = ActivityScenario.launch(Route.class);
        routeScenario.moveToState(Lifecycle.State.CREATED);

        ActivityScenario<DirectionsActivity> directionScenario =
                ActivityScenario.launch(DirectionsActivity.class);
        directionScenario.moveToState(Lifecycle.State.CREATED);

        directionScenario.onActivity(directionsActivity -> {

            // change our location to Capuchin,
            directionsActivity.setLocManually("capuchin");
            directionsActivity.offTrack();

            // change our location to Capuchin,
            directionsActivity.setLocManually("siamang");
            directionsActivity.offTrack();

            // change our location to Capuchin,
            directionsActivity.setLocManually("gorilla");
            directionsActivity.offTrack();

            // Should be updated to say we are at Capuchin Monkeys
            ArrayList<DirectionItem> exhibit_names = directionsActivity.getbriefDirections();

            String exhibit_name = exhibit_names.get(0).getName();

            assertEquals("Gorillas", exhibit_name);
            directionsActivity.dao.deleteAll();
        });
    }
}

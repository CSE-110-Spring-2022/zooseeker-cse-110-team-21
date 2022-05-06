package com.example.team21_zooseeker;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.widget.Button;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;

import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

@RunWith(AndroidJUnit4.class)
public class RouteTest {

    private SharedPreferences.Editor edt;
    private Context context;

    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    //@Rule
    //public ActivityScenarioRule<Route> routeScenarioRule = new ActivityScenarioRule<>(Route.class);

    @Before
    public void setup(){

    }

    @Test
    public void testAllExhibits(){
        ActivityScenario<MainActivity> mainScenario = scenarioRule.getScenario();

        mainScenario.moveToState(Lifecycle.State.CREATED);

        mainScenario.onActivity(activity -> {
            Set<String> fullZoo = new HashSet<String>();

            fullZoo.add("gorillas");
            fullZoo.add("arctic_foxes");
            fullZoo.add("elephant_odyssey");
            fullZoo.add("lions");
            fullZoo.add("gators");

            activity.setUserSelection("set", fullZoo);

        });

        ActivityScenario<Route>  routeScenario = ActivityScenario.launch(Route.class);
        routeScenario.moveToState(Lifecycle.State.CREATED);

        routeScenario.onActivity(routeActivity -> {

            RecyclerView rcview = routeActivity.findViewById(R.id.exhibit_text);
            RouteAdapter.ViewHolder holder = (RouteAdapter.ViewHolder) rcview.findViewHolderForAdapterPosition(0);

            assertEquals("Alligators, 110ft", holder.getExhibitDist());
        });
    }

    @Test
    public void testOneExhibitSelected(){
        ActivityScenario<MainActivity> mainScenario = scenarioRule.getScenario();

        mainScenario.moveToState(Lifecycle.State.CREATED);

        mainScenario.onActivity(activity -> {
            Set<String> fullZoo = new HashSet<String>();
            //Selected only the elephant_odyssey
            fullZoo.add("elephant_odyssey");
            activity.setUserSelection("set", fullZoo);

        });

        ActivityScenario<Route>  routeScenario = ActivityScenario.launch(Route.class);
        routeScenario.moveToState(Lifecycle.State.CREATED);

        routeScenario.onActivity(routeActivity -> {

            RecyclerView rcview = routeActivity.findViewById(R.id.exhibit_text);
            RouteAdapter.ViewHolder holder = (RouteAdapter.ViewHolder) rcview.findViewHolderForAdapterPosition(0);

            assertEquals("Elephant Odyssey, 510ft", holder.getExhibitDist());
        });
    }

    @Test
    public void testAllNearByExhibit(){
        ActivityScenario<MainActivity> mainScenario = scenarioRule.getScenario();

        mainScenario.moveToState(Lifecycle.State.CREATED);

        mainScenario.onActivity(activity -> {
            Set<String> fullZoo = new HashSet<String>();
            fullZoo.add("gorillas");
            fullZoo.add("arctic_foxes");
            fullZoo.add("gators");
            activity.setUserSelection("set", fullZoo);

        });

        ActivityScenario<Route>  routeScenario = ActivityScenario.launch(Route.class);
        routeScenario.moveToState(Lifecycle.State.CREATED);

        routeScenario.onActivity(routeActivity -> {

            RecyclerView rcview = routeActivity.findViewById(R.id.exhibit_text);
            RouteAdapter.ViewHolder holder = (RouteAdapter.ViewHolder) rcview.findViewHolderForAdapterPosition(0);

            assertEquals("Alligators, 110ft", holder.getExhibitDist());
        });
    }


}

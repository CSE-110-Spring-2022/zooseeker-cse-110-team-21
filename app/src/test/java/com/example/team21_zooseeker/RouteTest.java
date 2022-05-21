package com.example.team21_zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team21_zooseeker.activities.route.Route;
import com.example.team21_zooseeker.activities.route.RouteAdapter;
import com.example.team21_zooseeker.activities.search_select.SearchSelectActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

@RunWith(AndroidJUnit4.class)
public class RouteTest {

    private SharedPreferences.Editor edt;
    private Context context;

    @Rule
    public ActivityScenarioRule<SearchSelectActivity> scenarioRule = new ActivityScenarioRule<>(SearchSelectActivity.class);


    @Before
    public void setup(){

    }

    @Test
    public void testAllExhibits(){
        ActivityScenario<SearchSelectActivity> mainScenario = scenarioRule.getScenario();

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
        ActivityScenario<SearchSelectActivity> mainScenario = scenarioRule.getScenario();

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
        ActivityScenario<SearchSelectActivity> mainScenario = scenarioRule.getScenario();

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

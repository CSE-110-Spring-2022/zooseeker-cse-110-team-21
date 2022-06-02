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
import com.example.team21_zooseeker.helpers.ExhibitEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RouteTest {

    private SharedPreferences.Editor edt;
    private Context context;

    @Rule
    public ActivityScenarioRule<SearchSelectActivity> scenarioRule =
            new ActivityScenarioRule<>(SearchSelectActivity.class);


    @Before
    public void setup() {

    }

    @Test
    public void testAllExhibits() {
        ActivityScenario<SearchSelectActivity> mainScenario = scenarioRule.getScenario();

        mainScenario.moveToState(Lifecycle.State.CREATED);

        mainScenario.onActivity(activity -> {
            ExhibitEntity dbItem = new ExhibitEntity(activity.searchDataBase.node.get("flamingo"));
            activity.viewModel.insertExhibit(activity, dbItem);
            dbItem = new ExhibitEntity(activity.searchDataBase.node.get("crocodile"));
            activity.viewModel.insertExhibit(activity, dbItem);
            dbItem = new ExhibitEntity(activity.searchDataBase.node.get("parker_aviary"));
            activity.viewModel.insertExhibit(activity, dbItem);
            dbItem = new ExhibitEntity(activity.searchDataBase.node.get("owens_aviary"));
            activity.viewModel.insertExhibit(activity, dbItem);
            dbItem = new ExhibitEntity(activity.searchDataBase.node.get("koi"));
            activity.viewModel.insertExhibit(activity, dbItem);

        });

        ActivityScenario<Route> routeScenario = ActivityScenario.launch(Route.class);
        routeScenario.moveToState(Lifecycle.State.CREATED);

        routeScenario.onActivity(routeActivity -> {

            RecyclerView rcview = routeActivity.findViewById(R.id.selected_items);
            RouteAdapter.ViewHolder holder = (RouteAdapter.ViewHolder) rcview.findViewHolderForAdapterPosition(0);
            assertEquals("Flamingos, 5300ft", holder.getExhibitDist());

            holder = (RouteAdapter.ViewHolder) rcview.findViewHolderForAdapterPosition(1);
            assertEquals("Crocodiles, 12200ft", holder.getExhibitDist());

            holder = (RouteAdapter.ViewHolder) rcview.findViewHolderForAdapterPosition(2);
            assertEquals("Parker Aviary, 17500ft", holder.getExhibitDist());

            holder = (RouteAdapter.ViewHolder) rcview.findViewHolderForAdapterPosition(3);
            assertEquals("Owens Aviary, 18800ft", holder.getExhibitDist());

            holder = (RouteAdapter.ViewHolder) rcview.findViewHolderForAdapterPosition(4);
            assertEquals("Koi Fish, 31800ft", holder.getExhibitDist());

            routeActivity.dao.deleteAll();
        });
    }

    @Test
    public void testOneExhibitSelected() {
        ActivityScenario<SearchSelectActivity> mainScenario = scenarioRule.getScenario();

        mainScenario.moveToState(Lifecycle.State.CREATED);

        mainScenario.onActivity(activity -> {
            ExhibitEntity dbItem = new ExhibitEntity(activity.searchDataBase.node.get("parker_aviary"));
            activity.viewModel.insertExhibit(activity, dbItem);
        });

        ActivityScenario<Route> routeScenario = ActivityScenario.launch(Route.class);
        routeScenario.moveToState(Lifecycle.State.CREATED);

        routeScenario.onActivity(routeActivity -> {

            RecyclerView rcview = routeActivity.findViewById(R.id.selected_items);
            RouteAdapter.ViewHolder holder = (RouteAdapter.ViewHolder) rcview.findViewHolderForAdapterPosition(0);

            assertEquals("Parker Aviary, 7400ft", holder.getExhibitDist());
        });
    }
}

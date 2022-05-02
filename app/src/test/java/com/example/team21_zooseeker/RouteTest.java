package com.example.team21_zooseeker;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public ActivityScenarioRule<Route> routeScenarioRule = new ActivityScenarioRule<>(Route.class);

    @Before
    public void setup(){
        Context context = ApplicationProvider.getApplicationContext();
        preferences = context.getSharedPreferences("shared_prefs", MODE_PRIVATE);
        editor = preferences.edit();
        editor.clear().apply();
    }

    @Test
    public void testAllExhibits(){
        ActivityScenario<MainActivity> mainScenario = scenarioRule.getScenario();
        ActivityScenario<Route>  routeScenario = routeScenarioRule.getScenario();

        mainScenario.moveToState(Lifecycle.State.CREATED);

        mainScenario.onActivity(activity -> {

            Set<String> fullZoo = new HashSet<String>();

            fullZoo.add("gorillas");
            fullZoo.add("arctic_foxes");
            fullZoo.add("elephant_odyssey");
            fullZoo.add("lions");
            fullZoo.add("gators");

            editor.clear().apply();
            editor.putStringSet("set", fullZoo);
            editor.apply();
        });

        routeScenario.moveToState(Lifecycle.State.CREATED);

        routeScenario.onActivity(activity -> {

            RecyclerView rcview = activity.findViewById(R.id.exhibit_text);
            RouteAdapter.ViewHolder holder = (RouteAdapter.ViewHolder) rcview.findViewHolderForAdapterPosition(0);

            assertEquals("Alligators, 110.0ft", holder.getExhibitDist());
        });
    }
}

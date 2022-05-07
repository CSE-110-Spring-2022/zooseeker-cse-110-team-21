package com.example.team21_zooseeker;

import static org.junit.Assert.assertEquals;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;

@RunWith(AndroidJUnit4.class)
public class SelectAnimalsTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void exhibit_counter_test() {
        ActivityScenario<MainActivity> scenario = scenarioRule.getScenario();

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
        ActivityScenario<MainActivity> scenario = scenarioRule.getScenario();
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

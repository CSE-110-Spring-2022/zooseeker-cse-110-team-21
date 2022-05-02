package com.example.team21_zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import android.widget.AutoCompleteTextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchTest {
    ArrayList<String> animals = new ArrayList<>();

    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void addJson(){
        List<Node> node = Node.loadJSON(InstrumentationRegistry.getTargetContext(),"exhibits.json");

        for (int i = 0; i < node.size(); i++){
            if (node.get(i).kind.equals("exhibit")){
                animals.add(node.get(i).name);
            }
        }
    }

    /*
    Tests if the text is is correct with the input
     */
    @Test
    public void correctTextInTest() {
        ActivityScenario<MainActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {

            // InstrumentationRegistry.getTargetContext() gets the context without "this"

            String test = "Apple Pie";
            AutoCompleteTextView search = activity.findViewById(R.id.search_bar);
            search.setText("Apple Pie");
            assertEquals(search.getText().toString(), test);

            search.setText("AppleJack");
            assertNotEquals(search.getText().toString(), test);


            assertEquals(animals.contains("Arctic Foxes"), true);

        });

        scenario.onActivity(activity -> {

            // InstrumentationRegistry.getTargetContext() gets the context without "this"

            String test = "AppleJack";
            AutoCompleteTextView search = activity.findViewById(R.id.search_bar);


            assertEquals(search.getText().toString(), "AppleJack");

        });


    }

    // Tests if the names of animals are correctly in the search list
    @Test
    public void testJsonParse() {
        ActivityScenario<MainActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {

            assertEquals(animals.contains("Arctic Foxes"), true);

            assertEquals(animals.contains("Velociraptor"), false);

        });
    }


}

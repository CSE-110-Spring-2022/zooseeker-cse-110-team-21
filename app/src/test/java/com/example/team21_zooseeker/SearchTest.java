package com.example.team21_zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;
import android.widget.AutoCompleteTextView;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.team21_zooseeker.activities.search_select.SearchSelectActivity;
import com.example.team21_zooseeker.helpers.ExhibitDao;
import com.example.team21_zooseeker.helpers.ExhibitDatabase;
import com.example.team21_zooseeker.helpers.ZooData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchTest {
    ArrayList<String> animals = new ArrayList<>();

    @Rule
    public ActivityScenarioRule<SearchSelectActivity> scenarioRule = new ActivityScenarioRule<>(SearchSelectActivity.class);

    @Before
    public void addJson(){
       Map<String, ZooData.VertexInfo> node = ZooData.loadVertexInfoJSON(InstrumentationRegistry.getTargetContext(),"zoo_node_info.json");

        for (String str : node.keySet()){

            // checks if the current node is an animal
            if (node.get(str).kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)){
                animals.add(node.get(str).name);
            } }
    }

    /*
    Tests if the text is is correct with the input
     */
    @Test
    public void correctTextInTest() {
        ActivityScenario<SearchSelectActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {

            // InstrumentationRegistry.getTargetContext() gets the context without "this"

            String test = "Apple Pie";
            AutoCompleteTextView search = activity.findViewById(R.id.search_bar);
            search.setText("Apple Pie");
            assertEquals(test, search.getText().toString());

            search.setText("AppleJack");
            assertNotEquals(test, search.getText().toString());


            assertEquals(animals.contains("Gorillas"), true);

        });

        scenario.onActivity(activity -> {
            String test = "AppleJack";
            AutoCompleteTextView search = activity.findViewById(R.id.search_bar);

            assertEquals(search.getText().toString(), "AppleJack");
            assertNotEquals(search.getText().toString(), "Arctic");
        });
    }

    // Tests if the names of animals are correctly in the search list
    @Test
    public void testJsonParse() {
        ActivityScenario<SearchSelectActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {

            assertEquals(animals.contains("Gorillas"), true);

            assertEquals(animals.contains("Velociraptor"), false);

        });
    }


}

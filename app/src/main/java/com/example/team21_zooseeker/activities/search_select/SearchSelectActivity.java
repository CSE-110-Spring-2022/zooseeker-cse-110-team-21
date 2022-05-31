package com.example.team21_zooseeker.activities.search_select;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.activities.route.Route;
import com.example.team21_zooseeker.helpers.Alerts;
import com.example.team21_zooseeker.helpers.ExhibitDao;
import com.example.team21_zooseeker.helpers.ExhibitDatabase;
import com.example.team21_zooseeker.helpers.ExhibitEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SearchSelectActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public SearchDataBase searchDataBase;
    private AutoCompleteTextView search_bar;
    private TextView counterDisplay;
    public Set<String> selectedAnimals;
    public List<ExhibitEntity> selectedAnimalsDb;

    private SearchBuilder builder;

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;
    private ExhibitDao exhibitDao;

    public RecyclerView recyclerView;
    private SelectListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedAnimals = new HashSet<>();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_select);

        // Setup
        {
            prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
            editor = prefs.edit();

            search_bar = findViewById(R.id.search_bar);
            counterDisplay = findViewById(R.id.exhibit_counter);
            searchDataBase = new SearchDataBase();

            // Room Database Dao
            exhibitDao = ExhibitDatabase.getSingleton(this).exhibitDao();
            selectedAnimalsDb = new ArrayList<>();
        }

        // Build Search Database
        {
            builder = new SearchBuilder(this);
            builder.buildNodeList();
            builder.buildNameAndGroupId();
            builder.buildNameAndId();
            builder.buildNameTags();
            searchDataBase = builder.getSearchDatabase();
        }

        // Adding substring and tag search
        {
        /* custom arrayadapter for autocomplete
           allows for checking the subtring of an animal and its tags.
           Example: "Foxes", tag: "mammal".
           Before when ArrayAdapter was used, "Foxes" would only pop up
           if you typed the prefix of the word, i.e. "F~". Now we can type in "xes" and Foxes will pop up.
           Additionally, we can type in a substring of "mammal", e.g. "mm" and "Foxes" will appear as an option
           in the autocomplete.
        */
            StringFilterArrayAdapter adapter = new StringFilterArrayAdapter(this,
                    android.R.layout.simple_list_item_1, searchDataBase.name_tags);

            search_bar.setAdapter(adapter);
            search_bar.setThreshold(1);
        }

        // Recycle View for displaying selected exhibits
        {
            adapter = new SelectListAdapter();
            adapter.setHasStableIds(true);

            recyclerView = findViewById(R.id.selected_items);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            adapter.setExhibitItems(selectedAnimalsDb);
        }

        // animation for transitioning MainActivity using a fade
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // When user clicks on search result item, this will trigger onItemClick function that will
        // populate the selectedAnimals set accordingly.
        search_bar.setOnItemClickListener(this);
    }

    public void onPlanButtonClicked(View view) {
        if (selectedAnimals.isEmpty()) {
            Alerts.showAlert(this, "Your plan is empty! Please select some animals.");
            return;
        }
        setUserSelection("set", this.selectedAnimals);
        Intent intent = new Intent(this, Route.class);
        startActivity(intent);
    }

//    public List<ExhibitEntity> convertSetToList(Set<String> selectedAnimals) {
//        List<ExhibitEntity> selectedAnimalsDb = new ArrayList<>();
//        for (String id : selectedAnimals) {
//            ExhibitEntity dbItem = new ExhibitEntity(searchDataBase.node.get(id));
//            selectedAnimalsDb.add(dbItem);
//        }
//        return selectedAnimalsDb;
//    }

    @VisibleForTesting
    public void setUserSelection(String str, Set<String> userSelection){
        editor.clear().apply();
        editor.putStringSet(str, userSelection);
        editor.apply();
    }

    // Source: https://www.youtube.com/watch?v=0bLwXw5aFOs
    // gets speech from user and places the words into search_Bar
    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search_bar.setText(result.get(0), true);
        }
    }

    // This function will retrieve the name of the animal selected and add it to the selectedAnimals
    // set if it's not a duplicate selection.
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Fetch the user selected animal
        String animalID = searchDataBase.nameToId.get(parent.getItemAtPosition(position).toString());
        String groupID = searchDataBase.nameToGroupId.get(parent.getItemAtPosition(position).toString());

        // append to List of selected Animals or show an alert if it has already been selected
        int prevAnimalCount = selectedAnimals.size();
        selectedAnimals.add(groupID);
        if (prevAnimalCount == selectedAnimals.size()) {
            Alerts.showAlert(this, "You have already selected this animal.");
        }
        else {
            ExhibitEntity dbItem = new ExhibitEntity(searchDataBase.node.get(animalID));
            selectedAnimalsDb.add(dbItem);
            adapter.notifyDataSetChanged();
        }
        Log.d("exhibits: ", selectedAnimals.toString());

        // Update the exhibit counter
        this.counterDisplay.setText(String.valueOf(selectedAnimals.size()));
        // Clear search bar
        this.search_bar.setText("");
    }
}
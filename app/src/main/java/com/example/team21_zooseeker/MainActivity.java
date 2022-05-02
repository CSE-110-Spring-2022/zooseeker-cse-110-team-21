package com.example.team21_zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView search_bar;
    private TextView counterDisplay;
    private Set<String> selectedAnimals = new HashSet<String>();

    // ignore copied from
    // https://www.youtube.com/watch?v=JB3ETK5mh3c
    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get objects
        search_bar = findViewById(R.id.search_bar);
        counterDisplay = findViewById(R.id.exhibit_counter);

        List<String> animals = new ArrayList<>();
        List<Node> node = Node.loadJSON(this,"exhibits.json");
        for (int i = 0; i < node.size(); i++){
            if (node.get(i).kind.equals("exhibit")){
                animals.add(node.get(i).name);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, animals);
        search_bar.setAdapter(adapter);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // When user clicks on search result item, this will trigger onItemClick function that will
        // populate the selectedAnimals set accordingly.
        search_bar.setOnItemClickListener(this);
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
        String animal = parent.getItemAtPosition(position).toString();

        // append to List of selected Animals or show an alert if it has already been selected
        int prevAnimalCount = this.selectedAnimals.size();
        this.selectedAnimals.add(animal);
        if (prevAnimalCount == this.selectedAnimals.size()) {
            Utilities.showAlert(this, "You have already selected this animal.");
        }

        // Update the exhibit counter
        this.counterDisplay.setText(String.valueOf(selectedAnimals.size()));

        // Clear search bar
        this.search_bar.setText("");
    }
}
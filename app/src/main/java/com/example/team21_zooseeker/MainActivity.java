package com.example.team21_zooseeker;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView search_bar;
    private TextView counterDisplay;
    protected Set<String> selectedAnimals = new HashSet<String>();
    private Map<String, ZooData.VertexInfo> node;
    private Map<String, String> nameToId;


    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        editor = prefs.edit();

        // get objects
        search_bar = findViewById(R.id.search_bar);
        counterDisplay = findViewById(R.id.exhibit_counter);

        List<String> animals = new ArrayList<>();
        node = ZooData.loadVertexInfoJSON(this,"sample_node_info.json");

        nameToId = new HashMap<String, String>();

        for (String str : node.keySet()){
            if (node.get(str).kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)){
                animals.add(node.get(str).name);
            }
            nameToId.put(node.get(str).name, str);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, animals);
        search_bar.setAdapter(adapter);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // When user clicks on search result item, this will trigger onItemClick function that will
        // populate the selectedAnimals set accordingly.
        search_bar.setOnItemClickListener(this);
    }

    public void onPlanButtonClicked(View view) {
        if (this.selectedAnimals.isEmpty()) {
            Utilities.showAlert(this, "Your plan is empty! Please select some animals.");
            return;
        }
        setUserSelection("set", this.selectedAnimals);
        Intent intent = new Intent(this, Route.class);
        startActivity(intent);
    }

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
        String animal = nameToId.get(parent.getItemAtPosition(position).toString());

        // append to List of selected Animals or show an alert if it has already been selected
        int prevAnimalCount = this.selectedAnimals.size();
        this.selectedAnimals.add(animal);
        if (prevAnimalCount == this.selectedAnimals.size()) {
            Utilities.showAlert(this, "You have already selected this animal.");
        }
        Log.d("exhibits: ", this.selectedAnimals.toString());

        // Update the exhibit counter
        this.counterDisplay.setText(String.valueOf(selectedAnimals.size()));
        // Clear search bar
        this.search_bar.setText("");
    }
}
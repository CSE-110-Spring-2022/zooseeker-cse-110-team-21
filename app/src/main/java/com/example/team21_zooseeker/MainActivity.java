package com.example.team21_zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView search_bar;
    public  ArrayList<String> animalNames;

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
        search_bar = findViewById(R.id.search_bar);

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
}
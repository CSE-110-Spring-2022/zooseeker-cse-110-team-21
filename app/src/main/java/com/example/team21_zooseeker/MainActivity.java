package com.example.team21_zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText search_bar;
    private List<String> selectedAnimals = new ArrayList<>(Arrays.asList());
    private Button selectButton;
    private Button selectButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search_bar = findViewById(R.id.search_bar);

        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this,"sample_node_info.json");

        this.selectButton = (Button)findViewById(R.id.button);
        String firstVertex = vInfo.get(vInfo.keySet().toArray()[0]).getName();
        this.selectButton.setText(firstVertex);

        this.selectButton2 = (Button)findViewById(R.id.button2);
        String secondVertex = vInfo.get(vInfo.keySet().toArray()[1]).getName();
        this.selectButton2.setText(secondVertex);

        Log.d("vInfo", vInfo.toString());
    }

    // Source: https://www.youtube.com/watch?v=0bLwXw5aFOs
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
            search_bar.setText(result.get(0));

        }
    }

    public void onAnimalButtonClick(View view) {
        // Push our animal object_id into List<object_id>
        this.selectButton = (Button)findViewById(view.getId());
        this.selectedAnimals.add(selectButton.getText().toString());
        for (String animal : selectedAnimals) {
            Log.d("animal name", animal);
        }
    }
}
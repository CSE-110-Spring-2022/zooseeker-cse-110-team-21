package com.example.team21_zooseeker;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        editor = prefs.edit();

        Set<String> selections = new HashSet<String>();
        selections.add("gorillas");
        selections.add("elephant_odyssey");
        setUserSelection("set", selections);
    }

    public void OnPlanButtonClicked(View view) {
        Intent intent = new Intent(this, Route.class);
        startActivity(intent);
    }

    @VisibleForTesting
    public void setUserSelection(String str, Set<String> userSelection){
        editor.clear().apply();
        editor.putStringSet(str, userSelection);
        editor.apply();
    }
}
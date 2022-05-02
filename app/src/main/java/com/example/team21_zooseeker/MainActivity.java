package com.example.team21_zooseeker;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> selections = new HashSet<String>();
        selections.add("gorillas");
        selections.add("elephant_odyssey");
        editor.putStringSet("set", selections);
        editor.apply();
    }

    public void OnPlanButtonClicked(View view) {
        Intent intent = new Intent(this, Route.class);
        startActivity(intent);
    }
}
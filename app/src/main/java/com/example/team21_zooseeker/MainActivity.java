package com.example.team21_zooseeker;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void OnPlanButtonClicked(View view) {
        Intent intent = new Intent(this, Route.class);
        startActivity(intent);
    }
}
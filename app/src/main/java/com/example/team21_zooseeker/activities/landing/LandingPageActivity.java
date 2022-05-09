package com.example.team21_zooseeker.activities.landing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.team21_zooseeker.activities.search_select.SearchSelectActivity;
import com.example.team21_zooseeker.R;

public class LandingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

    }

    public void start(View v){
        Intent intent = new Intent(this, SearchSelectActivity.class);
        startActivity(intent);
    }

}
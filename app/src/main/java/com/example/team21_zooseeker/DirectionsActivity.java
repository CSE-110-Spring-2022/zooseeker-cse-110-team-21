package com.example.team21_zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectionsActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    Button nextBtn, prevBtn;
    ArrayList<DirectionItem> directions = new ArrayList<DirectionItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        // get views
        viewPager = findViewById(R.id.view_pager);
        nextBtn = findViewById(R.id.next_btn);
        prevBtn = findViewById(R.id.prev_btn);

        directions = SharedPrefs.loadList(this, "directions");

        DirectionsAdapter directionsAdapter = new DirectionsAdapter(directions);
        viewPager.setAdapter(directionsAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setUserInputEnabled(false);

        // set previous btn to be invisible initially
        prevBtn.setVisibility(View.INVISIBLE);
        if (directions.size() == 1)
            nextBtn.setVisibility(View.INVISIBLE);
    }

    public void onNextBtnClicked(View view) {
        int currentIndex = viewPager.getCurrentItem();
        viewPager.setCurrentItem(currentIndex + 1, true);
        setBtnInvisibilities(currentIndex + 1);
    }

    public void onPrevBtnClicked(View view) {
        int currentIndex = viewPager.getCurrentItem();
        viewPager.setCurrentItem(currentIndex - 1, true);
        setBtnInvisibilities(currentIndex - 1);
    }

    public void setBtnInvisibilities(int index) {
        int exhibitCounter = directions.size();
        Log.d("index", String.valueOf(index));

        if (index == exhibitCounter - 1) {
            nextBtn.setVisibility(View.INVISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
        }
        else if (index == 0) {
            nextBtn.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.INVISIBLE);
        }
        else {
            nextBtn.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
        }
    }
}
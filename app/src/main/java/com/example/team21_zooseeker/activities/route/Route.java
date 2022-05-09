package com.example.team21_zooseeker.activities.route;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.View;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.helpers.SharedPrefs;
import com.example.team21_zooseeker.activities.directions.DirectionsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Route extends AppCompatActivity {
    //TODO: Move hardcoded strings into constants
    //TODO: This includes sharedprefs name and key, start node, and JSON file names

    private RouteCalc routeCalc;
    private List<String> directions;

    public SharedPreferences preferences;
    public RecyclerView recyclerView;
    public SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        routeCalc = new RouteCalc(this);

        //User Selection set in sharedPreferences as a Set<String>
        preferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        editor = preferences.edit();

        Set<String> userSelectionSet = preferences.getStringSet("set", null);
        List<String> userSelection = new ArrayList<String>();

        //RouteCalc works with List<String>, so we convert the Set to a list
        if(userSelectionSet != null) {
            for (String s : userSelectionSet) {
                userSelection.add(s);
            }
        }

        // start (and, by proxy, end) of each route
        String start = "entrance_exit_gate";

        //offload the responsibility of calculation to the routeCalc class...
        List<String> initialList = routeCalc.initialDirections(start, userSelection);


        //So that we can focus on the screen display!
        RouteAdapter adapter = new RouteAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.exhibit_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //call to subList makes it so the exit gate isn't shown in the overview
        adapter.setDirections(initialList.subList(0, initialList.size() - 1));

        SharedPrefs.saveList(this, routeCalc.directions, "directions");
    }

    public void onBeginDirectionsClicked(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);


        startActivity(intent);
    }
}
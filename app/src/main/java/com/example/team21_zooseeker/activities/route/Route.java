package com.example.team21_zooseeker.activities.route;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.util.Log;
import android.view.View;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.activities.directions.DirectionItem;
import com.example.team21_zooseeker.activities.search_select.SelectListAdapter;
import com.example.team21_zooseeker.helpers.ExhibitDao;
import com.example.team21_zooseeker.helpers.ExhibitDatabase;
import com.example.team21_zooseeker.helpers.ExhibitEntity;
import com.example.team21_zooseeker.helpers.SharedPrefs;
import com.example.team21_zooseeker.activities.directions.DirectionsActivity;
import com.example.team21_zooseeker.helpers.StringFormat;
import com.example.team21_zooseeker.helpers.ViewModel;

import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Route extends AppCompatActivity {
    //TODO: Move hardcoded strings into constants
    //TODO: This includes sharedprefs name and key, start node, and JSON file names

    private RouteCalc routeCalc;
    private List<DirectionItem> briefDirections;
    private List<DirectionItem> detailedDirections;
    private StringFormat sf;

    private List<ExhibitEntity> exhibitEntities;

    public SharedPreferences preferences;
    public RecyclerView recyclerView;
    public SharedPreferences.Editor editor;
    public Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        intent = new Intent(this, DirectionsActivity.class);

        routeCalc = new RouteCalc(this);
        sf = new StringFormat(this);

        //User Selection set in sharedPreferences as a Set<String>
        preferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        editor = preferences.edit();

//        Set<String> userSelectionSet = preferences.getStringSet("set", null);

        // View Model
        {
            ExhibitDao dao = ExhibitDatabase.getSingleton(this).exhibitDao();
            exhibitEntities = dao.getAll();
        }

        Set<String> userSelectionSet = exhibitEntities.stream()
                .map(v -> v.group_id == null ? v.id : v.group_id)
                .collect(Collectors.toSet());

        ArrayList<String> userSelection = new ArrayList<String>();

        //RouteCalc works with List<String>, so we convert the Set to a list
        if(userSelectionSet != null) {
            for (String s : userSelectionSet) {
                userSelection.add(s);
            }
        }

      SharedPrefs.saveStrList(this, userSelection, this.getString(R.string.USER_SELECT));

        // start (and, by proxy, end) of each route
        String start = this.getString(R.string.ENTRANCE_EXIT);

        //offload the responsibility of calculation to the routeCalc class...
        List<GraphPath<String, IdentifiedWeightedEdge>> route = routeCalc.calculateRoute(start, userSelection);
        List<String> initialList = sf.initialDirections(route);



        detailedDirections = sf.getDirections(route, true);

        briefDirections = sf.getDirections(route, false);
        //detailedDirections = sf.getDirections(route, true);

       // sf.printDebugInfo(route);

        SharedPrefs.saveList(this, new ArrayList<DirectionItem>(briefDirections), "directions");
        SharedPrefs.saveList(this, new ArrayList<DirectionItem>(detailedDirections) , "detailed_dirs");



        //So that we can focus on the screen display!
        RouteAdapter adapter = new RouteAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.selected_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //call to subList makes it so the exit gate isn't shown in the overview
        adapter.setDirections(initialList.subList(0, initialList.size() - 1));
    }

    public void onBeginDirectionsClicked(View view) {
        startActivity(intent);
    }

    public void onBackBtnClicked(View view) {
        finish();
    }
}
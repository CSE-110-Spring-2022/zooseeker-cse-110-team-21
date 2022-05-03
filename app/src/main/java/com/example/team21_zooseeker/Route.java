package com.example.team21_zooseeker;


import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;
import org.jgrapht.nio.json.JSONImporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Route extends AppCompatActivity {

    private Graph<String, IdentifiedWeightedEdge> g;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;

    public SharedPreferences preferences;

    public RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        preferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);

        Set<String> userSelectionSet = preferences.getStringSet("fullZoo", null);
        List<String> userSelection = new ArrayList<String>();

        if(userSelectionSet != null) {
            for (String s : userSelectionSet) {
                userSelection.add(s);
            }
        }

        // "source" and "sink" are graph terms for the start and end
        String start = "entrance_exit_gate";

        // 1. Load the graph...
        g = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");

        // 2. Load the information about our nodes and edges...
        vInfo = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");

        List<String> initialList = initialDirections(start, userSelection);
        for(String s : initialList){
            System.out.println(s);
        }

        RouteAdapter adapter = new RouteAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.exhibit_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setDirections(initialList);
    }

    void singleShortestPath(String start, String goal){

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, goal);
        System.out.printf("The shortest path from '%s' to '%s' is:\n", start, goal);

        int i = 1;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            System.out.printf("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                    i,
                    g.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    vInfo.get(g.getEdgeSource(e).toString()).name,
                    vInfo.get(g.getEdgeTarget(e).toString()).name);
            i++;
        }
    }

    GraphPath<String, IdentifiedWeightedEdge> findNextClosestExhibit(String current, List<String> exhibits){
        double minWeight = Double.MAX_VALUE;
        GraphPath<String, IdentifiedWeightedEdge> nextPath = null;
        for(String exhibit : exhibits) {

            GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, current, exhibit);
            if(path.getWeight() < minWeight){
                minWeight = path.getWeight();
                nextPath = path;
            }
        }
        return nextPath;
    }

    List<GraphPath<String, IdentifiedWeightedEdge>> calculateRoute(String start, List<String> exhibits){
        List<GraphPath<String, IdentifiedWeightedEdge>> route =
                new ArrayList<GraphPath<String, IdentifiedWeightedEdge>>();
        String current = start;
        GraphPath<String, IdentifiedWeightedEdge> nextPath = null;

        while((nextPath = findNextClosestExhibit(current, exhibits)) != null){
            route.add(nextPath);
            exhibits.remove(current);
            current = vInfo.get(nextPath.getEndVertex()).id;
            exhibits.remove(current);
        }

        route.add(DijkstraShortestPath.findPathBetween(g, current, start));

        //print statements to test

        for(GraphPath<String, IdentifiedWeightedEdge> path : route){
            int count = 1;

            List<String> vertices = path.getVertexList();
            int size = vertices.size();
            for(int i = 0; i < size-1; i++){
                IdentifiedWeightedEdge e = g.getEdge(vertices.get(i), vertices.get(i+1));
                System.out.printf("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                        count,
                        g.getEdgeWeight(e),
                        eInfo.get(e.getId()).street,
                        vInfo.get(vertices.get(i)).name,
                        vInfo.get(vertices.get(i+1)).name);
                count++;
            }
            System.out.printf("-------------------------------------------\n");
        }

        return route;
    }

    List<String> initialDirections(String start, List<String> exhibits){
        List<GraphPath<String, IdentifiedWeightedEdge>> route = calculateRoute(start, exhibits);
        List<String> dispStrings = new ArrayList<String>();

        double weight = 0;
        for(GraphPath<String, IdentifiedWeightedEdge> path : route){
            weight += path.getWeight();
            String exhibit = vInfo.get(path.getEndVertex()).name;

            dispStrings.add(exhibit + ", " + weight + "ft");
        }
        return dispStrings;
    }
}
package com.example.team21_zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;
import org.jgrapht.nio.json.JSONImporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Route extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        //Create graph
        Graph<String, DefaultWeightedEdge> g = null;
        try {
            //Reads in graph from JSON file
            g = createGraphFromJSON(this,"sample_graph.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Tests the generated graph against a shortest algorithm function from jgrpaht
        calculateShortestPath(g);

        finish();
    }

    public void calculateShortestPath(Graph<String, DefaultWeightedEdge> g) {
        GraphPath<String, DefaultWeightedEdge> shortest_path = DijkstraShortestPath.findPathBetween(g, "entranceExitGate1", "arcticFoxViewpoint");
        System.out.println("Shortest path from entranceExitGate1 to arcticFoxViewpoint: \n" + shortest_path.toString());
    }

    public static Graph<String, DefaultWeightedEdge> createGraphFromJSON(Context context, String path) throws IOException {
        Graph<String, DefaultWeightedEdge> g = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        JSONImporter<String, DefaultWeightedEdge> jsonImporter = new JSONImporter<>();
        jsonImporter.setVertexFactory(label -> label);

        InputStream input = context.getAssets().open(path);
        Reader reader = new InputStreamReader(input);
        jsonImporter.importGraph(g, reader);

        return g;
    }
}
package com.example.team21_zooseeker.activities.route;

import android.util.Pair;

import com.example.team21_zooseeker.helpers.ZooData;
import com.google.android.gms.maps.model.LatLng;

import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OffTrackCalc {
    public static final double DEG_LAT = 363843.57;
    public static final double DEG_LNG = 307515.50;


    public static final LatLng test_one = new LatLng(5, -100);
    public static final LatLng test_two = new LatLng(8, -30);

    //@TODO
    public static LatLng compare(LatLng one, LatLng two) {
        return new LatLng(0,0);
    }

    public List<GraphPath<String, IdentifiedWeightedEdge>> calculateRoute(double currLat, double currLng, List<String> exhibits,
                                                                          Map<String, ZooData.VertexInfo> vInfo, RouteCalc rc){
        List<GraphPath<String, IdentifiedWeightedEdge>> route =
                new ArrayList<GraphPath<String, IdentifiedWeightedEdge>>();

        Pair<Double,Double> currLoc = new Pair<>(currLat,currLng);

        double minDist = Double.MAX_VALUE;
        String minExhibit = "";

        for (String e : exhibits){
            Pair<Double, Double> eLoc = new Pair<>(Double.parseDouble(vInfo.get(e).lat),Double.parseDouble(vInfo.get(e).lng));
            double currDist = distance(currLoc,eLoc);
            if (currDist < minDist){
                minDist = currDist;
                minExhibit = e;
            }
        }

        exhibits.remove(minExhibit);

        List<GraphPath<String, IdentifiedWeightedEdge>> rest = rc.calculateRoute(minExhibit, exhibits);
        route.addAll(rest);
        return route;
    }


    public static double distance (Pair<Double,Double> l1, Pair<Double,Double> l2){
        double base = 100;
        double d_lat = Math.abs(l1.first - l2.first) * DEG_LAT;
        double d_lng = Math.abs(l1.second - l2.second) * DEG_LNG;

        double d_ft = Math.sqrt(Math.pow(d_lat,2) + Math.pow(d_lng,2));
        return base * Math.ceil(d_ft/base);
    }
}



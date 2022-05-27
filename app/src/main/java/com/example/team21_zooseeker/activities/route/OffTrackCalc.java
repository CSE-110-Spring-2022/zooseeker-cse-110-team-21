package com.example.team21_zooseeker.activities.route;

import android.util.Pair;

import com.example.team21_zooseeker.helpers.ZooData;

import java.util.Map;

public class OffTrackCalc {
    public static final double DEG_LAT = 363843.57;
    public static final double DEG_LNG = 307515.50;

    public String calculateRoute(double currLat, double currLng, Map<String, ZooData.VertexInfo> vInfo){

        Pair<Double,Double> currLoc = new Pair<>(currLat,currLng);

        double minDist = Double.MAX_VALUE;
        String minVertex = "";

        for (String v : vInfo.keySet()){
            Pair<Double, Double> vLoc = new Pair<>(Double.parseDouble(vInfo.get(v).lat),Double.parseDouble(vInfo.get(v).lng));
            double currDist = distance(currLoc,vLoc);
            if (currDist < minDist){
                minDist = currDist;
                minVertex = v;
            }
        }

        return minVertex;
    }

    public static double distance (Pair<Double,Double> l1, Pair<Double,Double> l2){
        double base = 100;
        double d_lat = Math.abs(l1.first - l2.first) * DEG_LAT;
        double d_lng = Math.abs(l1.second - l2.second) * DEG_LNG;

        double d_ft = Math.sqrt(Math.pow(d_lat,2) + Math.pow(d_lng,2));
        return base * Math.ceil(d_ft/base);
    }
}



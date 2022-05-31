package com.example.team21_zooseeker.activities.route;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.team21_zooseeker.helpers.ZooData;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

public class OffTrackCalc {
    public static final double DEG_LAT = 363843.57;
    public static final double DEG_LNG = 307515.50;
    public static double currLat = 0;
    public static double currLong = 0;

//    public static Pair <String,Double> distanceToNextExhibit(Map<String, ZooData.VertexInfo> vInfo, String exhibit) {
//
//        Pair<Double, Double> currLoc = new Pair<>(currLat, currLong);
//
//        Pair<Double, Double> vLoc = new Pair<>(Double.parseDouble(vInfo.get(exhibit).lat), Double.parseDouble(vInfo.get(exhibit).lng));
//        double currDist = distance(currLoc, vLoc);
//
//
//        return new Pair<String,Double>(exhibit, currDist);
//    }
//
//    public static Pair <String,Double> calculateNextClosestExhibit(Map<String, ZooData.VertexInfo> vInfo, ArrayList<String> exhibits) {
//
//        Pair<Double, Double> currLoc = new Pair<>(currLat, currLong);
//
//        double minDist = Double.MAX_VALUE;
//        String minVertex = "";
//
//        for (String v : exhibits) {
//            Pair<Double, Double> vLoc = new Pair<>(Double.parseDouble(vInfo.get(v).lat), Double.parseDouble(vInfo.get(v).lng));
//            double currDist = distance(currLoc, vLoc);
//            if (currDist < minDist) {
//                minDist = currDist;
//                minVertex = v;
//            }
//        }
//
//        return new Pair<String,Double>(minVertex,minDist);
//    }

    public static Pair <String,Double> nextClosestVertex(Map<String, ZooData.VertexInfo> vInfo) {

        Pair<Double, Double> currLoc = new Pair<>(currLat, currLong);

        double minDist = Double.MAX_VALUE;
        String minVertex = "";

        for (String v : vInfo.keySet()) {
            Pair<Double, Double> vLoc = new Pair<>(Double.parseDouble(vInfo.get(v).lat), Double.parseDouble(vInfo.get(v).lng));
            double currDist = distance(currLoc, vLoc);
            if (currDist < minDist) {
                minDist = currDist;
                minVertex = v;
            }
        }

        return new Pair<String,Double>(minVertex,minDist);
    }

    public static double distance(Pair<Double, Double> l1, Pair<Double, Double> l2) {
        double base = 100;
        double d_lat = Math.abs(l1.first - l2.first) * DEG_LAT;
        double d_lng = Math.abs(l1.second - l2.second) * DEG_LNG;

        double d_ft = Math.sqrt(Math.pow(d_lat, 2) + Math.pow(d_lng, 2));
        return base * Math.ceil(d_ft / base);
    }

    @SuppressLint("MissingPermission")
    public static void locationUpdate(Context context) {
        {
            var provider = LocationManager.GPS_PROVIDER;
            var locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            var locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    //Log.d("LocationChanged", String.format("Location changed: %s", location));

                    var marker = new MarkerOptions()
                            .position(new LatLng(
                                    location.getLatitude(),
                                    location.getLongitude())).
                                    title("Navigation step");
                    currLong = location.getLongitude();
                    currLat = location.getLatitude();

                }
            };

            locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
        }
    }
}



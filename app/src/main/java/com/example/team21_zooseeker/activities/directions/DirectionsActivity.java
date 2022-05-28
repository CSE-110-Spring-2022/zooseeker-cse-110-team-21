package com.example.team21_zooseeker.activities.directions;

import static com.example.team21_zooseeker.activities.route.OffTrackCalc.locationUpdate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.activities.route.IdentifiedWeightedEdge;
import com.example.team21_zooseeker.activities.route.Route;
import com.example.team21_zooseeker.activities.route.RouteCalc;
import com.example.team21_zooseeker.activities.route.userLocation;
import com.example.team21_zooseeker.helpers.SharedPrefs;
import com.example.team21_zooseeker.helpers.StringFormat;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DirectionsActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    Button nextBtn, prevBtn;
    ToggleButton toggleDesc;
    ArrayList<DirectionItem> detailedDirections = new ArrayList<DirectionItem>();
    ArrayList<DirectionItem> briefDirections = new ArrayList<DirectionItem>();
    DirectionsAdapter directionsAdapter;
    userLocation loc;
    RouteCalc rc;
    StringFormat sf;

    ArrayList<String> userSel;
    ArrayList<String> userVisited;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        loc = new userLocation(this, this);
        rc = new RouteCalc(this);
        sf = new StringFormat(this);
        userSel = SharedPrefs.loadStrList(this, this.getString(R.string.USER_SELECT));
        userVisited = new ArrayList<String>();



        // get views
        viewPager = findViewById(R.id.view_pager);
        nextBtn = findViewById(R.id.next_btn);
        prevBtn = findViewById(R.id.prev_btn);

        briefDirections = SharedPrefs.loadList(this, "directions");
        detailedDirections = SharedPrefs.loadList(this, "detailed_dirs");

        directionsAdapter = new DirectionsAdapter(briefDirections);
        viewPager.setAdapter(directionsAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setUserInputEnabled(false);

        //Toggle event
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleDetail);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    directionsAdapter.setDirectionsList(detailedDirections);
                    directionsAdapter.notifyDataSetChanged();
                    toggle.setChecked(true);
                } else {
                    // The toggle is disabled
                    directionsAdapter.setDirectionsList(briefDirections);
                    directionsAdapter.notifyDataSetChanged();
                    toggle.setChecked(false);
                }
            }
        });

        // set previous btn to be invisible initially
        prevBtn.setVisibility(View.INVISIBLE);
        if (briefDirections.size() == 1)
            nextBtn.setVisibility(View.INVISIBLE);
        else
            nextBtn.setText(briefDirections.get(viewPager.getCurrentItem() + 1).getName());

        loc.setId("crocodile");

    }

    public void onNextBtnClicked(View view) {
        int currentIndex = viewPager.getCurrentItem();
        viewPager.setCurrentItem(currentIndex + 1, true);
        setBtnFeatures(currentIndex + 1);

        // updates by clicking next button
        locationUpdate(this);

    }

    public void onPrevBtnClicked(View view) {
        int currentIndex = viewPager.getCurrentItem();
        viewPager.setCurrentItem(currentIndex - 1, true);
        setBtnFeatures(currentIndex - 1);
    }

    /**
     * onUpdate
     *
     * Called whenever userLocation is set in class userLocation
     * First, determines whether or not the closest exhibit is still
     * the current exhibit
     *
     * If it is, updates the directions on the card
     *
     * If a different exhibit is closer, delegate to Off-Track Suggestions
     * @param id Vertex ID of the user's new location
     */
    public void onUpdate(String id){
        //userSel must be kept updated; if a user has visited particular locations

        int ind = viewPager.getCurrentItem();
        ArrayList<DirectionItem> curr_list = directionsAdapter.getDirectionsList();

        //GraphPath<String, IdentifiedWeightedEdge> rePath = rc.findNextClosestExhibit(id, userSel);
        String goal = "";
        for(String key : sf.vInfo.keySet()){
            if(sf.vInfo.get(key).getName().equals(curr_list.get(ind).getName())){
                goal = key;
            }
        }

        System.out.println("SOURCE: " + id);
        System.out.println("SINK: " + goal);

        GraphPath<String, IdentifiedWeightedEdge> rePath = rc.singleShortestPath(id, goal);

        ArrayList<GraphPath<String, IdentifiedWeightedEdge>> strList = new ArrayList<GraphPath<String, IdentifiedWeightedEdge>>();
        strList.add(rePath);
        List<DirectionItem> strList1 = sf.getDirections(strList, true);
        List<DirectionItem> strList2 = sf.getDirections(strList, false);

        toggleDesc = (ToggleButton) findViewById(R.id.toggleDetail);
        if(toggleDesc.isChecked()){
            curr_list.remove(ind);
            curr_list.add(ind, strList1.get(0));
            directionsAdapter.setDirectionsList(curr_list);
            detailedDirections = curr_list;

            briefDirections.remove(ind);
            briefDirections.add(ind, strList2.get(0));
        }else {
            curr_list.remove(ind);
            curr_list.add(ind, strList2.get(0));
            directionsAdapter.setDirectionsList(curr_list);
            briefDirections = curr_list;

            detailedDirections.remove(ind);
            detailedDirections.add(ind,strList1.get(0));
        }

        directionsAdapter.notifyDataSetChanged();

        if(id.equals(curr_list.get(ind).getName())){
            userSel.remove(id);
            userVisited.add(id);
        }

        System.out.println(sf.vInfo.get(curr_list.get(ind).getName()));
        GraphPath<String, IdentifiedWeightedEdge> closePath = rc.findNextClosestExhibit(id, userSel);
        if(!closePath.getEndVertex().equals(sf.vInfo.get(curr_list.get(ind).getName()))){
            promptOffTrack();
        }
    }

    public void offTrack(){
        ArrayList<DirectionItem> briefDir = new ArrayList<DirectionItem>();
        ArrayList<DirectionItem> detailedDir = new ArrayList<DirectionItem>();

        if(userVisited.size() > 0){
            List<GraphPath<String, IdentifiedWeightedEdge>> prevPath = rc.calculateRoute(this.getString(R.string.ENTRANCE_EXIT), userVisited);

            //removes excess path to end
            prevPath.remove(prevPath.size() -1);

            List<DirectionItem> prevDirsBrief = sf.getDirections(prevPath, false);
            List<DirectionItem> prevDirsDetailed = sf.getDirections(prevPath, true);

            briefDir.addAll(prevDirsBrief);
            detailedDir.addAll(prevDirsDetailed);
        }

        if(userSel.size() > 0){
            String start;
            start = loc.loc_id;

            List<GraphPath<String, IdentifiedWeightedEdge>> currPath = rc.calculateRoute(start, userSel);

            List<DirectionItem> currDirsBrief = sf.getDirections(currPath, false);
            List<DirectionItem> currDirsDetailed = sf.getDirections(currPath, true);

            briefDir.addAll(currDirsBrief);
            detailedDir.addAll(currDirsDetailed);
        }
        else{
            String current = sf.vInfo.get((briefDir.get(briefDir.size() - 1).getName())).id;
            ArrayList<GraphPath<String, IdentifiedWeightedEdge>> lastItem = new ArrayList<GraphPath<String, IdentifiedWeightedEdge>>();
            lastItem.add(DijkstraShortestPath.findPathBetween(rc.g, current, this.getString(R.string.ENTRANCE_EXIT)));

            briefDir.addAll(sf.getDirections(lastItem, false));
            detailedDir.addAll(sf.getDirections(lastItem, true));
        }

        briefDirections = briefDir;
        detailedDirections = detailedDir;

        toggleDesc = (ToggleButton) findViewById(R.id.toggleDetail);
        if(toggleDesc.isChecked()){
            directionsAdapter.setDirectionsList(detailedDirections);
        }
        else {
            directionsAdapter.setDirectionsList(briefDirections);
        }

        directionsAdapter.notifyDataSetChanged();
    }

    /*
    * Code for AlertDialog adapted from:
    *  https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
    */
    public void promptOffTrack(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Off Track!");
        builder.setMessage("You are closer to a different exhibit. Reroute?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                offTrack();
                System.out.println("POSITIVE BUTTON");
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.out.println("NEGATIVE");
            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

    public void setBtnFeatures(int index) {
        int exhibitCounter = briefDirections.size();
        System.out.println("SIZE: " + exhibitCounter);
        Log.d("index", String.valueOf(index));

        if (index == exhibitCounter - 1) {
            nextBtn.setVisibility(View.INVISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
            prevBtn.setText(briefDirections.get(viewPager.getCurrentItem() - 1).getName());
        }
        else if (index == 0) {
            nextBtn.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.INVISIBLE);
            nextBtn.setText(briefDirections.get(viewPager.getCurrentItem() + 1).getName());
        }
        else {
            nextBtn.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setText(briefDirections.get(viewPager.getCurrentItem() + 1).getName());
            prevBtn.setText(briefDirections.get(viewPager.getCurrentItem() - 1).getName());
        }
    }

    public void onBackBtnClicked(View view) {
        finish();
    }





}
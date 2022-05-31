package com.example.team21_zooseeker.activities.directions;

import static com.example.team21_zooseeker.activities.route.OffTrackCalc.locationUpdate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.activities.route.IdentifiedWeightedEdge;
import com.example.team21_zooseeker.activities.route.OffTrackCalc;
import com.example.team21_zooseeker.activities.route.RouteCalc;
import com.example.team21_zooseeker.activities.route.userLocation;
import com.example.team21_zooseeker.helpers.SharedPrefs;
import com.example.team21_zooseeker.helpers.StringFormat;
import com.example.team21_zooseeker.helpers.ZooData;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class DirectionsActivity extends AppCompatActivity {
    private final ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;
    private boolean annoyUser;


    ViewPager2 viewPager;
    Button nextBtn, prevBtn;
    ToggleButton toggleDesc;
    ArrayList<DirectionItem> detailedDirections = new ArrayList<DirectionItem>();
    ArrayList<DirectionItem> briefDirections = new ArrayList<DirectionItem>();
    DirectionsAdapter directionsAdapter;
    userLocation loc;
    RouteCalc rc;
    StringFormat sf;
    ArrayList<String> exhibits;
    ArrayList<String> userSel;
    //ArrayList<String> userVisited;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        // setup
        {
            loc = new userLocation(this, this);
            rc = new RouteCalc(this);
            sf = new StringFormat(this);
            exhibits = SharedPrefs.loadStrList(this, this.getString(R.string.USER_SELECT));
            userSel = SharedPrefs.loadStrList(this, this.getString(R.string.USER_SELECT));
            annoyUser = true;

        }

        // get views
        {
            viewPager = findViewById(R.id.view_pager);
            nextBtn = findViewById(R.id.next_btn);
            prevBtn = findViewById(R.id.prev_btn);
        }

        // gets brief and detailed directions
        {
            briefDirections = SharedPrefs.loadList(this, "directions");
            detailedDirections = SharedPrefs.loadList(this, "detailed_dirs");
        }

        directionsAdapter = new DirectionsAdapter(briefDirections);
        viewPager.setAdapter(directionsAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setUserInputEnabled(false);


        //Toggle event
        {
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

        }

        // set previous btn to be invisible initially
        prevBtn.setVisibility(View.INVISIBLE);

        if (briefDirections.size() == 1)
            nextBtn.setVisibility(View.INVISIBLE);
        else
            nextBtn.setText(briefDirections.get(viewPager.getCurrentItem() + 1).getName());

        // Runs in Background to check if off track
        {
            this.future = backgroundThreadExecutor.submit(() -> {
                do {
                    // updates location
                    runOnUiThread(() -> {
                        locationUpdate(this);
                    });

                    Thread.sleep(2000);

                    Pair<String, Double> n = OffTrackCalc.nextClosestVertex(sf.vInfo);

                    runOnUiThread(() -> {
                        Log.d("Curr Loc", n.first);
                                loc.setId(n.first);
                            });

                } while (true);

            });

        }
    }

    public void onNextBtnClicked(View view) {
        int currentIndex = viewPager.getCurrentItem();
        viewPager.setCurrentItem(currentIndex + 1, true);
        setBtnFeatures(currentIndex + 1);


        annoyUser = true;

    }

    public void onPrevBtnClicked(View view) {
        int currentIndex = viewPager.getCurrentItem();
        viewPager.setCurrentItem(currentIndex - 1, true);
        setBtnFeatures(currentIndex - 1);
        annoyUser = true;
    }

    /**
     * onUpdate
     *
     * Called whenever userLocation is set in class userLocation
     *
     * First, updates current views
     * Then, it determines if the userLocation is closest to the current location
     * If not, a check to prompt Off-Track is called
     * @param id Vertex ID of the user's new location
     */
    public void onUpdate(String id){
        //userSel must be kept updated; if a user has visited particular locations

        int ind = viewPager.getCurrentItem();
        ArrayList<DirectionItem> curr_list = directionsAdapter.getDirectionsList();

        //loop to get key from exhibit name
        String goal = "";
        for(String key : sf.vInfo.keySet()){
            if(sf.vInfo.get(key).getName().equals(curr_list.get(ind).getName())){
                goal = key;
            }
        }

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

        GraphPath<String, IdentifiedWeightedEdge> closePath = rc.findNextClosestExhibit(id, userSel);
        if(!closePath.getEndVertex().equals((curr_list.get(ind).getId())) && annoyUser){
            promptOffTrack();
        }
    }

    /**
     * offTrack
     *
     * Called when "skip" is pressed, or when
     * "yes" is selected in the Off-track prompt
     *
     * Partitions the userSelection set into previous items,
     * and next items. It preserves previous item order and recalculates
     * for the next items.
     *
     * Note: calls to calculateRoute change the list
     * that is passed in, so copies of the lists must be passed to maintain
     * functionality.
     */
    public void offTrack(){
        ArrayList<DirectionItem> briefDir = new ArrayList<DirectionItem>();
        ArrayList<DirectionItem> detailedDir = new ArrayList<DirectionItem>();

        ArrayList<DirectionItem> curr_list = directionsAdapter.getDirectionsList();
        ArrayList<String> userVisited = new ArrayList<String>();
        int ind = viewPager.getCurrentItem();
        for(int i = 0; i < ind; i++){

            //loop to get keys from names
            String goal = "";
            for(String key : sf.vInfo.keySet()){
                if(sf.vInfo.get(key).getName().equals(curr_list.get(i).getName())){
                    goal = key;
                    Log.d("GOAL: ", goal);

                }
            }
            userVisited.add(goal);
        }

        briefDirections.clear();
        detailedDirections.clear();

        if(userVisited.size() > 0){
            ArrayList<String> userVisitedCopy = new ArrayList<String>(userVisited);
            List<GraphPath<String, IdentifiedWeightedEdge>> prevPath = rc.calculateRoute(this.getString(R.string.ENTRANCE_EXIT), userVisitedCopy);

            //removes excess path to end
            prevPath.remove(prevPath.size() -1);

            List<DirectionItem> prevDirsBrief = sf.getDirections(prevPath, false);
            List<DirectionItem> prevDirsDetailed = sf.getDirections(prevPath, true);

            briefDir.addAll(prevDirsBrief);
            detailedDir.addAll(prevDirsDetailed);
        }

        ArrayList<String> userSelCopy = new ArrayList<String>(userSel);
        for(String str: userVisited){
            userSelCopy.remove(str);
        }

        if(userSelCopy.size() > 0){
            String start;
            start = loc.loc_id;

            List<GraphPath<String, IdentifiedWeightedEdge>> currPath = rc.calculateRoute(start, userSelCopy);

            List<DirectionItem> currDirsBrief = sf.getDirections(currPath, false);
            List<DirectionItem> currDirsDetailed = sf.getDirections(currPath, true);

            briefDir.addAll(currDirsBrief);
            detailedDir.addAll(currDirsDetailed);
        }
        else{

            String current = "";
            if(briefDir.size() > 0) {
                //loop to get key from names
                String goal = "";
                for (String key : sf.vInfo.keySet()) {
                    if (sf.vInfo.get(key).getName().equals(briefDir.get(briefDir.size() - 1).getName())) {
                        goal = key;
                    }
                }
                current = sf.vInfo.get(goal).id;
            }
            else{
                current = this.getString(R.string.ENTRANCE_EXIT);
            }
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
        setBtnFeatures(ind);
    }

    /*
    * Code for AlertDialog adapted from:
    *  https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
    *
    * Logic for prompting the user when Off-Track
    */
    public void promptOffTrack(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Off Track!");
        builder.setMessage("You are closer to a different exhibit. Reroute?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                offTrack();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                annoyUser = false;
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

    /**
     * onSkipBtnClicked
     *
     * Called whenever 'skip' button is clicked
     *
     * If there is more than one item in the userSelection,
     * and the user is not on the last page:
     * -removes exhibit from the userSelection list
     * -calls offTrack() to recalculate the route
     *
     * @param view
     */
    public void onSkipBtnClicked(View view){

        annoyUser = true;
        ArrayList<DirectionItem> curr_list = directionsAdapter.getDirectionsList();
        int ind = viewPager.getCurrentItem();

        //loop to get key from name
        String goal = "";
        for(String key : sf.vInfo.keySet()){
            if(sf.vInfo.get(key).getName().equals(curr_list.get(ind).getName())){
                goal = key;
            }
        }

        if(userSel.size() > 1 && (ind != curr_list.size()-1)) {
            userSel.remove(goal);
            offTrack();
        }
    }

    public void onBackBtnClicked(View view) {
        this.future.cancel(true);
        finish();
    }

}
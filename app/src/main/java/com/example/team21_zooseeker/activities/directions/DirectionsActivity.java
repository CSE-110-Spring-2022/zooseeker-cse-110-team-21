package com.example.team21_zooseeker.activities.directions;

import static com.example.team21_zooseeker.activities.route.OffTrackCalc.locationUpdate;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DirectionsActivity extends AppCompatActivity {
    private final ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;
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
    Map<String,ZooData.VertexInfo> vInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        loc = new userLocation(this, this);
        rc = new RouteCalc(this);
        sf = new StringFormat(this);
        exhibits = SharedPrefs.loadStrList(this,this.getString(R.string.USER_SELECT));
        vInfo = ZooData.loadVertexInfoJSON(this, this.getString(R.string.NODE_INFO));

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

        this.future = backgroundThreadExecutor.submit(()-> {
            do {
                // updates location
                runOnUiThread(()->{
                    Log.d("runs","run");
                    locationUpdate(this);
                });

                Thread.sleep(5000);
                Pair<String,Double> vd = OffTrackCalc.calculateRoute(OffTrackCalc.currLat,OffTrackCalc.currLong,vInfo);
                String vertex = vd.first;
                Double distance = vd.second;

                if (distance > 7000){
                    runOnUiThread(()->{
                            Log.d("off-track","off");
                    });
                    break;

                }
            } while (true);

            return null;
        });
    }

    public void onNextBtnClicked(View view) {
        int currentIndex = viewPager.getCurrentItem();
        viewPager.setCurrentItem(currentIndex + 1, true);
        setBtnFeatures(currentIndex + 1);


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
        //userSel must be kept updated; if a user has visited particular locations,
        //they must be removed from the list kept in SharedPrefs.
        ArrayList<String> userSel = SharedPrefs.loadStrList(this, this.getString(R.string.USER_SELECT));

        ArrayList<DirectionItem> curr_list = directionsAdapter.getDirectionsList();
        GraphPath<String, IdentifiedWeightedEdge> rePath = rc.findNextClosestExhibit(id, userSel);
        ArrayList<GraphPath<String, IdentifiedWeightedEdge>> strList = new ArrayList<GraphPath<String, IdentifiedWeightedEdge>>();
        strList.add(rePath);
        List<DirectionItem> strList1 = sf.getDirections(strList, true);
        List<DirectionItem> strList2 = sf.getDirections(strList, false);
        toggleDesc = (ToggleButton) findViewById(R.id.toggleDetail);
        int ind = viewPager.getCurrentItem();
        if(toggleDesc.isChecked()){
            curr_list.remove(ind);
            curr_list.add(ind, strList1.get(0));
            directionsAdapter.setDirectionsList(curr_list);
        }else {
            curr_list.remove(ind);
            curr_list.add(ind, strList2.get(0));
            directionsAdapter.setDirectionsList(curr_list);
        }
        directionsAdapter.notifyDataSetChanged();



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
        this.future.cancel(true);
        finish();
    }





}
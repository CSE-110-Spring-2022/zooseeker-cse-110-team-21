package com.example.team21_zooseeker.activities.directions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.activities.route.IdentifiedWeightedEdge;
import com.example.team21_zooseeker.activities.route.Route;
import com.example.team21_zooseeker.activities.route.RouteCalc;
import com.example.team21_zooseeker.activities.route.userLocation;
import com.example.team21_zooseeker.helpers.SharedPrefs;
import com.example.team21_zooseeker.helpers.StringFormat;

import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;

public class DirectionsActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    Button nextBtn, prevBtn;
    ArrayList<DirectionItem> directions = new ArrayList<DirectionItem>();
    DirectionsAdapter directionsAdapter;
    userLocation loc;
    RouteCalc rc;
    StringFormat sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        loc = new userLocation(this, this);
        rc = new RouteCalc(this);
        sf = new StringFormat(this);

        // get views
        viewPager = findViewById(R.id.view_pager);
        nextBtn = findViewById(R.id.next_btn);
        prevBtn = findViewById(R.id.prev_btn);

        directions = SharedPrefs.loadList(this, "directions");

        directionsAdapter = new DirectionsAdapter(directions);
        viewPager.setAdapter(directionsAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setUserInputEnabled(false);

        // set previous btn to be invisible initially
        prevBtn.setVisibility(View.INVISIBLE);
        if (directions.size() == 1)
            nextBtn.setVisibility(View.INVISIBLE);
        else
            nextBtn.setText(directions.get(viewPager.getCurrentItem() + 1).getName());

        loc.setId("gators");
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
        List<DirectionItem> strPath = sf.getDirections(strList, false);

        int ind = viewPager.getCurrentItem();
        if(curr_list.get(ind).getName().equals(strPath.get(0).getName())) {
            curr_list.remove(ind);
            curr_list.add(ind, strPath.get(0));
            directionsAdapter.setDirectionsList(curr_list);
            directionsAdapter.notifyDataSetChanged();
        }
        else{
            System.out.println("Better Route Detected!");
            //TODO delegate to off-track suggestions
        }
    }

    public void setBtnFeatures(int index) {
        int exhibitCounter = directions.size();
        System.out.println("SIZE: " + exhibitCounter);
        Log.d("index", String.valueOf(index));

        if (index == exhibitCounter - 1) {
            nextBtn.setVisibility(View.INVISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
            prevBtn.setText(directions.get(viewPager.getCurrentItem() - 1).getName());
        }
        else if (index == 0) {
            nextBtn.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.INVISIBLE);
            nextBtn.setText(directions.get(viewPager.getCurrentItem() + 1).getName());
        }
        else {
            nextBtn.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setText(directions.get(viewPager.getCurrentItem() + 1).getName());
            prevBtn.setText(directions.get(viewPager.getCurrentItem() - 1).getName());
        }
    }

    public void onBackBtnClicked(View view) {
        finish();
    }


}
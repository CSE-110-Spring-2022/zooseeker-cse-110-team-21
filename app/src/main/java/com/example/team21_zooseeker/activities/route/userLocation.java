package com.example.team21_zooseeker.activities.route;

import android.content.Context;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.activities.directions.DirectionsActivity;
import com.example.team21_zooseeker.helpers.ZooData;

public class userLocation {
    public String loc_id;
    public DirectionsActivity dir_act;

    //ctor takes context, sets to entrance/exit by default
    public userLocation(DirectionsActivity da, Context context){
        loc_id = context.getString(R.string.ENTRANCE_EXIT);
        this.dir_act = da;
    }

    //ctor takes context and string id of a vertex
    public userLocation(DirectionsActivity da, String id){
        this.loc_id = id;
        this.dir_act = da;
    }

    public void setId(String id){
        if(!id.equals(this.loc_id)) {
            this.loc_id = id;
            update(loc_id);
        }
    }

    private void update(String id){
        dir_act.onUpdate(id);
    }
}

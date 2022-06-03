package com.example.team21_zooseeker.activities.landing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.team21_zooseeker.activities.search_select.SearchSelectActivity;
import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.helpers.PermissionChecker;
import com.example.team21_zooseeker.helpers.SharedPrefs;

public class LandingPageActivity extends AppCompatActivity {

    private final PermissionChecker permissionChecker = new PermissionChecker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        if (permissionChecker.ensurePermissions()) return;

        int loadIndex = SharedPrefs.loadInt(this, "directions_index");
        Log.d("loadIndex LandingPage", String.valueOf(loadIndex));
        if (loadIndex > -1) {
            startActivity(new Intent(this, SearchSelectActivity.class));
        }
    }

    public void start(View v){
        Intent intent = new Intent(this, SearchSelectActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //SharedPrefs.saveInt(this, -1, "directions_index");
        Log.d("onStop Landingpage:", "called");
    }

}
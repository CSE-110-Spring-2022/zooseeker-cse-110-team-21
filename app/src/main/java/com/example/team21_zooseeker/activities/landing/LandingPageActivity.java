package com.example.team21_zooseeker.activities.landing;

import static com.example.team21_zooseeker.helpers.ChooseAsset.edgeInfo;
import static com.example.team21_zooseeker.helpers.ChooseAsset.nodeInfo;
import static com.example.team21_zooseeker.helpers.ChooseAsset.zooGraph;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.team21_zooseeker.activities.search_select.SearchSelectActivity;
import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.helpers.PermissionChecker;

public class LandingPageActivity extends AppCompatActivity {

    private final PermissionChecker permissionChecker = new PermissionChecker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        if (permissionChecker.ensurePermissions()) return;
    }

    public void start(View v){
        Intent intent = new Intent(this, SearchSelectActivity.class);
        startActivity(intent);
    }

    public void changeAssetsMS1(View v){
        zooGraph = this.getString(R.string.ZOO_GRAPH);
        nodeInfo = this.getString(R.string.NODE_INFO);
        edgeInfo = this.getString(R.string.EDGE_INFO);
    }

    public void changeAssetsMS2(View v) {
        zooGraph = this.getString(R.string.MS2_ZOO_GRAPH);
        nodeInfo = this.getString(R.string.MS2_NODE_INFO);
        edgeInfo = this.getString(R.string.MS2_EDGE_INFO);
    }
}
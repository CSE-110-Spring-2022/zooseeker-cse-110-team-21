package com.example.team21_zooseeker.activities.search_select;

import android.content.Context;
import android.util.Pair;

import com.example.team21_zooseeker.helpers.ZooData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SearchDataBase {
    public Map<String, ZooData.VertexInfo> node;
    public Map<String, String> nameToId;
    public ArrayList<Pair<String, String>> name_tags;

    public SearchDataBase() {
    }

    public SearchDataBase(Map<String, ZooData.VertexInfo> node, Map<String, String> nameToId,
                          ArrayList<Pair<String, String>> name_tags) {
        this.node = node;
        this.nameToId = nameToId;
        this.name_tags = name_tags;
    }

    //--------Get Methods---------\\
    public Map<String, ZooData.VertexInfo> getNode() {
        return node;
    }

    public Map<String, String> getNameToId() {
        return nameToId;
    }

    public ArrayList<Pair<String, String>> getName_tags() {
        return name_tags;
    }
    //--------Get Methods---------\\

}
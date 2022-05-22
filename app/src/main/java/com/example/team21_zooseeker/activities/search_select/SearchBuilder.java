package com.example.team21_zooseeker.activities.search_select;


import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.team21_zooseeker.helpers.ZooData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SearchBuilder {
    public Map<String, ZooData.VertexInfo> node;
    public Map<String, String> nameToId;
    public ArrayList<Pair<String, String>> name_tags;

    private Context context;


    public SearchBuilder(Context context) {
        this.context = context;
        nameToId = new HashMap<String, String>();
        name_tags = new ArrayList<Pair<String, String>>();
    }

    public void buildExhibitGroup() {

    }
    public void buildNodeList() {
        node = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
    }

    public void buildNameAndId() {
        for (String id : node.keySet()){

            // nodes with parent_id will use the parent_id rather than id
            // this is b/c edge edge_info.json will
            // use the parent_id rather than the id for directions
            if (node.get(id).parent_id != null) {
                nameToId.put(node.get(id).name, node.get(id).parent_id);
            } else {
                nameToId.put(node.get(id).name, id);
            }
            Log.d("buildNameAnd ID", node.get(id).name + ", " + id);
        }
    }

    public void buildNameTags() {
        for (String str : node.keySet()){

            // checks if the current node is an animal
            if (node.get(str).kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)){
                // places a pair of an animal and its tags into name_tags
                Pair<String, String> temp_animal = new Pair<>(node.get(str).name, node.get(str).getTag());
                name_tags.add(temp_animal);
            }
        }
    }

    public SearchDataBase getSearchDatabase() {
        return new SearchDataBase(node, nameToId, name_tags);
    }

}

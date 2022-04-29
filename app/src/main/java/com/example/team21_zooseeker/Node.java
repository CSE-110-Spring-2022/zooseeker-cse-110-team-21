package com.example.team21_zooseeker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    public String id;
    public String kind;
    public String name;
    public ArrayList<String> tags;

    public Node (String id, String kind, String name, ArrayList<String> tags){
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", kind='" + kind + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}' + "\n";
    }

    public static List<Node> loadJSON (Context context, String path){
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Node>>(){}.getType();
            return gson.fromJson(reader,type);
        }
        catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

}

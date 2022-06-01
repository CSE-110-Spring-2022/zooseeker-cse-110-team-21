package com.example.team21_zooseeker.helpers;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "exhibit_items")
public class ExhibitEntity {
    // 1. Public Fields
    @PrimaryKey(autoGenerate = true)
    public long dbId;

    public static enum Kind {
        // The SerializedName annotation tells GSON how to convert
        // from the strings in our JSON to this Enum.
        @SerializedName("gate") GATE,
        @SerializedName("exhibit") EXHIBIT,
        @SerializedName("intersection") INTERSECTION,
        @SerializedName("exhibit_group") EXHIBIT_GROUP
    }

    @NonNull
    public String id;

    @Override
    public String toString() {
        return "ExhibitEntity{" +
                "group_id='" + group_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String group_id;
    public ZooData.VertexInfo.Kind kind;
    public String name;
    public String lat;
    public String lng;

    public Boolean visited;

    // 2. Constructor
    public ExhibitEntity(@NonNull String id, @NonNull String group_id, ZooData.VertexInfo.Kind kind,
                         @NonNull String name, @NonNull String lat, @NonNull String lng) {
        this.id = id;
        this.group_id = group_id;
        this.kind = kind;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.visited = false;
    }

    // 3. Overloaded Constructor
    public ExhibitEntity(ZooData.VertexInfo node) {
        this.id = node.id;
        this.group_id = node.group_id;
        this.kind = node.kind;
        this.name = node.name;
        this.lat = node.lat;
        this.lng = node.lng;
        this.visited = false;
    }
}

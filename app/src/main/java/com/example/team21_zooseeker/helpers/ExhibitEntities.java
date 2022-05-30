package com.example.team21_zooseeker.helpers;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName="exhibits")
public class ExhibitEntities {

    //public fields
    public static enum Kind {
        // The SerializedName annotation tells GSON how to convert
        // from the strings in our JSON to this Enum.
        @SerializedName("gate") GATE,

        @SerializedName("exhibit") EXHIBIT,

        @SerializedName("intersection") INTERSECTION,

        @SerializedName("exhibit_group") EXHIBIT_GROUP
    }

    @PrimaryKey(autoGenerate = true)
    public long dataT_ID;

    @NonNull
    public String id;
    @NonNull
    public String group_id;
    public ZooData.VertexInfo.Kind kind;
    @NonNull
    public String name;
    public List<String> tags;

    // Convert to Latlng later
    @NonNull
    public String lat;
    @NonNull
    public String lng;

    //Constructor

    public ExhibitEntities(@NonNull String id, @NonNull String group_id, ZooData.VertexInfo.Kind kind,
                           @NonNull String name, List<String> tags, @NonNull String lat, @NonNull String lng) {
        this.id = id;
        this.group_id = group_id;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
        this.lat = lat;
        this.lng = lng;
    }
}

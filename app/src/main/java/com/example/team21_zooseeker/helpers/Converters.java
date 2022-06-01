package com.example.team21_zooseeker.helpers;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Converters {
    public Set<String> entityListToStringSet(List<ExhibitEntity> exhibits) {
        Set<String> set = new HashSet<>();
        for (ExhibitEntity exhibit : exhibits) {
            set.add(exhibit.group_id);
        }
        return set;
    }

    @TypeConverter
    public static List<ExhibitEntity> fromExhibitEntity(String value) {
        Type listType = new TypeToken<List<ExhibitEntity>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<ExhibitEntity> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

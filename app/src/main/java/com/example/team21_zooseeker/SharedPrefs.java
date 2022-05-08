package com.example.team21_zooseeker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SharedPrefs {
    public static void saveMap(Context context, String key, Map<String, String> inputMap) {
        SharedPreferences pSharedPref = context.getSharedPreferences("shared_prefs",
                Context.MODE_PRIVATE);
        if (pSharedPref != null){
            Gson gson = new Gson();
            String hashMapString = gson.toJson(inputMap);
            //save in shared prefs
            pSharedPref.edit().putString(key, hashMapString).apply();
        }
    }

    public static Map<String, String> loadMap(String key, Context context) {
        Map<String, String> outputMap = new HashMap<String, String>();
        SharedPreferences pSharedPref = context.getSharedPreferences("shared_prefs",
                Context.MODE_PRIVATE);
        try{
            //get from shared prefs
            String storedHashMapString = pSharedPref.getString(key, (new JSONObject()).toString());
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            Gson gson = new Gson();
            return  gson.fromJson(storedHashMapString, type);

        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
}

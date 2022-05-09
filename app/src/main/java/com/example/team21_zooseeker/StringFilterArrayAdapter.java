package com.example.team21_zooseeker;

import android.content.Context;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

// adapted from https://stackoverflow.com/questions/24545345/how-to-bind-autocompletetextview-from-hashmap
public class StringFilterArrayAdapter extends ArrayAdapter implements Filterable {

    // this list will contain the options that appears on the autocomplete drop down list
    ArrayList<Pair<String, String>> listFiltered;
    ArrayList<  Pair<String, String>> original;

    public StringFilterArrayAdapter(@NonNull Context context, int resource, @NonNull  ArrayList<  Pair<String, String>> objects) {
        super(context, resource, objects);
        listFiltered = objects;
        original = objects;
    }

    @Override
    public int getCount() {
        return listFiltered.size();
    }

    // gets the name of the animal
    @Override
    public String getItem(int position) {
        return listFiltered.get(position).first;
    }

    // This method will determine what appears on the dropdown list for autocomplete
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override

            /*

                @param
                    constraint: The entire word in the searchbar after each click of the keyboard.
                        E.g "a", "ab", "abc"
             */
            protected FilterResults performFiltering(final CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    // We will return this list to change the options that appears
                    ArrayList<Pair<String, String>> autoCompleteList = new ArrayList<>();

                    // loops through each animal and compares the names and tags
                    for (int i = 0; i < original.size(); i++) {
                        String name = original.get(i).first.toLowerCase();
                        String tag = original.get(i).second.toLowerCase();

                        // adds to the filter if any letter in the constraint is contained in the tags or animal name
                        if ((tag.contains(constraint.toString().toLowerCase()) && !constraint.toString().contains(","))|| name.contains(constraint.toString().toLowerCase())) {
                            autoCompleteList.add(original.get(i));
                        }
                    }

                    filterResults.values = autoCompleteList;

                    filterResults.count = autoCompleteList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    listFiltered = (ArrayList<Pair<String, String>>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}


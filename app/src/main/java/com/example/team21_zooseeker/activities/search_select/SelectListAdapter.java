package com.example.team21_zooseeker.activities.search_select;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.helpers.ExhibitEntity;

import java.util.Collections;
import java.util.List;

public class SelectListAdapter extends RecyclerView.Adapter<SelectListAdapter.ViewHolder> {
    private List<ExhibitEntity> selectedAnimalsDb = Collections.emptyList();

    public void setExhibitItems(List<ExhibitEntity> newItems) {
        this.selectedAnimalsDb.clear();
        this.selectedAnimalsDb = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.selected_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setExhibitItem(selectedAnimalsDb.get(position));
    }

    @Override
    public int getItemCount() {
        return selectedAnimalsDb.size();
    }

    public long getItemId(int position) {
        return selectedAnimalsDb.get(position).dbId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private ExhibitEntity exhibitItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.selected_exhibit_name);
        }

        public ExhibitEntity getExhibitItem() {
            return exhibitItem;
        }

        public void setExhibitItem(ExhibitEntity exhibitItem) {
            this.exhibitItem = exhibitItem;
            this.textView.setText(exhibitItem.name);
        }
    }
}

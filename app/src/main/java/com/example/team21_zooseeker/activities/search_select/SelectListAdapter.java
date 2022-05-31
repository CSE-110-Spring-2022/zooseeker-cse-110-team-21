package com.example.team21_zooseeker.activities.search_select;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.helpers.ExhibitEntity;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SelectListAdapter extends RecyclerView.Adapter<SelectListAdapter.ViewHolder> {
    private List<ExhibitEntity> selectedAnimalsDb = Collections.emptyList();
    private Consumer<ExhibitEntity> onDeleteClicked;
    private TextView counterDisplay;

    public void setExhibitItems(List<ExhibitEntity> newItems) {
        this.selectedAnimalsDb.clear();
        this.selectedAnimalsDb = newItems;
        notifyDataSetChanged();
    }

    public void setOnDeleteClicked(Consumer<ExhibitEntity> onDeleteClicked, TextView counterDisplay) {
        this.onDeleteClicked = onDeleteClicked;
        this.counterDisplay =  counterDisplay;
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

    public ExhibitEntity getItem(int position) {
        return selectedAnimalsDb.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private ExhibitEntity exhibitItem;
        private TextView deleteView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.selected_exhibit_name);
            this.deleteView = itemView.findViewById(R.id.delete_btn);

            this.deleteView.setOnClickListener(view -> {
                if (onDeleteClicked == null) return;
                onDeleteClicked.accept(exhibitItem);
                counterDisplay.setText(String.valueOf(getItemCount() - 1));
            });
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

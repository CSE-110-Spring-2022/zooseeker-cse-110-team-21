package com.example.team21_zooseeker.activities.directions;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team21_zooseeker.R;

import java.util.ArrayList;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder> {
    ArrayList<DirectionItem> directionsList;

    public DirectionsAdapter(ArrayList<DirectionItem> directionsList) {
        this.directionsList = directionsList;
    }

    public void setDirectionsList(ArrayList<DirectionItem> dl){
        this.directionsList = dl;
    }

    public ArrayList<DirectionItem> getDirectionsList(){
        return directionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.directions_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DirectionItem directionsText = directionsList.get(position);

        String title = "";
        if (position + 1 == this.getItemCount())
            title = "End of Itinerary";
        else
            title = "Exhibit " + (position + 1);

        holder.exhibitTitle.setText(title);
        holder.exhibitName.setText(directionsText.getName());
        holder.directionsDesc.setText(directionsText.getDirDesc());
        holder.directionsDesc.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public int getItemCount() {
        return directionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView exhibitTitle, exhibitName, directionsDesc;
        Button nextBtn, prevBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            exhibitTitle = itemView.findViewById(R.id.exhibit_title);
            exhibitName = itemView.findViewById(R.id.exhibit_name);
            directionsDesc = itemView.findViewById(R.id.directions_description);
            nextBtn = itemView.findViewById(R.id.next_btn);
            prevBtn = itemView.findViewById(R.id.prev_btn);
        }
    }
}

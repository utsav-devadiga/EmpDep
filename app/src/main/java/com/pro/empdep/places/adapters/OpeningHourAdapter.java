package com.pro.empdep.places.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.empdep.R;

import java.util.ArrayList;
import java.util.List;

public class OpeningHourAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> hoursList;
    Context context;

    public OpeningHourAdapter(List<String> hoursList, Context context) {
        this.hoursList = hoursList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opening_hours_item, parent, false);
        return new OpenHoursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((OpenHoursViewHolder) holder).hours.setText(hoursList.get(position));
    }

    @Override
    public int getItemCount() {
        return hoursList.size();
    }

    public static class OpenHoursViewHolder extends RecyclerView.ViewHolder {
        TextView hours;

        public OpenHoursViewHolder(@NonNull View itemView) {
            super(itemView);
            hours = itemView.findViewById(R.id.hours);
        }
    }
}

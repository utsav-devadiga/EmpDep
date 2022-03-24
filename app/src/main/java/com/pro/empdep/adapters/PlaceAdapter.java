package com.pro.empdep.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.pro.empdep.R;
import com.pro.empdep.model.Places;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Places> placesList = new ArrayList<>();

    public PlaceAdapter(List<Places> placesList) {
        this.placesList = placesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_item, parent, false);
        return new PlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        ((PlacesViewHolder) holder).place_image.setImageResource(placesList.get(position).getImg());
        ((PlacesViewHolder) holder).places_name.setText(placesList.get(position).getName());
        ((PlacesViewHolder) holder).place_ratings.setText(placesList.get(position).getRating());
        ((PlacesViewHolder) holder).place_location.setText(placesList.get(position).getLocation());
        ((PlacesViewHolder) holder).place_price.setText(placesList.get(position).getRate());


    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public static class PlacesViewHolder extends RecyclerView.ViewHolder {

        public ImageView place_image;
        MaterialCardView place_card;
        TextView places_name, place_ratings, place_location, place_price;


        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            place_image = itemView.findViewById(R.id.place_item_image);
            place_card = itemView.findViewById(R.id.main_place_card);
            places_name = itemView.findViewById(R.id.place_name);
            place_ratings = itemView.findViewById(R.id.place_ratings);
            place_location = itemView.findViewById(R.id.place_location);
            place_price = itemView.findViewById(R.id.place_price);

        }
    }
}

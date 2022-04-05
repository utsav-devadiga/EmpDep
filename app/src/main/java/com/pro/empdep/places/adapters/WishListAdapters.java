package com.pro.empdep.places.adapters;

import static com.pro.empdep.places.constants.AppConstant.API_KEY_PHOTO_REF;
import static com.pro.empdep.places.constants.AppConstant.BASE_URL;
import static com.pro.empdep.places.constants.AppConstant.PHOTO_REF;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.R;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.places.adapters.PlacesApiAdapter;
import com.pro.empdep.places.interfaces.PlacesDetails;
import com.pro.empdep.places.interfaces.WishLists;
import com.pro.empdep.places.model.PlacesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WishListAdapters extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    View view;
    Context context;
    ArrayList<PlacesModel> placesArrayList;
    PlacesDetails placesDetails;

    public WishListAdapters( Context context, ArrayList<PlacesModel> placesArrayList, PlacesDetails placesDetails) {
        this.context = context;
        this.placesArrayList = placesArrayList;
        this.placesDetails = placesDetails;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_places_api_wishlist_trips, parent, false);
        return new WishListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(placesArrayList.get(position).getGeometry().getLocation().getLat(), placesArrayList.get(position).getGeometry().getLocation().getLng(), 1);

            Glide.with(context)
                    .load(BASE_URL + PHOTO_REF +
                            placesArrayList.get(position).getPhotos().get(0).getPhotoReference() +
                            API_KEY_PHOTO_REF)
                    .transform(new FitCenter(), new RoundedCorners(28))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(((WishListViewHolder) holder).placeImage);

            String cityName = addresses.get(0).getSubLocality();
            String stateName = addresses.get(0).getLocality();
            String countryName = addresses.get(0).getCountryName();
            ((WishListViewHolder) holder).placeName.setText(placesArrayList.get(position).getName());
            ((WishListViewHolder) holder).placesLocation.setText(cityName + ", " + stateName);




        } catch (Exception e) {
            e.printStackTrace();
        }

        ((WishListViewHolder) holder).places_item_main.setOnClickListener(view -> {
            placesDetails.onClickPlace(placesArrayList.get(position).getPlaceId());
        });

    }

    @Override
    public int getItemCount() {
        return placesArrayList.size();
    }


    public static class WishListViewHolder extends RecyclerView.ViewHolder{
        ImageView placeImage;
        TextView placeName, placesLocation;
        MaterialCardView places_item_main;

        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.places_image);
            placeName = itemView.findViewById(R.id.place_name);
            placesLocation = itemView.findViewById(R.id.place_location);
            places_item_main = itemView.findViewById(R.id.places_item_main);
        }
    }
}

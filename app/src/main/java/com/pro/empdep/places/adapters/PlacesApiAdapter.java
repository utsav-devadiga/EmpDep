package com.pro.empdep.places.adapters;

import static com.pro.empdep.places.constants.AppConstant.API_KEY_PHOTO_REF;
import static com.pro.empdep.places.constants.AppConstant.BASE_URL;
import static com.pro.empdep.places.constants.AppConstant.PHOTO_REF;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
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
import com.pro.empdep.places.interfaces.PlacesDetails;
import com.pro.empdep.places.interfaces.WishLists;
import com.pro.empdep.places.model.PlacesModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlacesApiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    View view;
    Context context;
    ArrayList<PlacesModel> placesArrayList;
    PlacesDetails placesDetails;
    WishLists wishLists;
    FirebaseFirestore db;
    FirebaseAuth auth;
    Geocoder geocoder;

    public PlacesApiAdapter(Context context, ArrayList<PlacesModel> placesArrayList, PlacesDetails placesDetails,
                            WishLists wishLists) {
        this.context = context;
        this.placesArrayList = placesArrayList;
        this.placesDetails = placesDetails;
        this.wishLists = wishLists;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_places_api, parent, false);
        return new PlacesApiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(placesArrayList.get(position).getGeometry().getLocation().getLat(), placesArrayList.get(position).getGeometry().getLocation().getLng(), 1);

            Glide.with(context)
                    .load(BASE_URL + PHOTO_REF +
                            placesArrayList.get(position).getPhotos().get(0).getPhotoReference() +
                            API_KEY_PHOTO_REF)
                    .transform(new FitCenter(), new RoundedCorners(28))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(((PlacesApiViewHolder) holder).placeImage);

            String cityName = (addresses.get(0).getSubLocality() != null) ? addresses.get(0).getSubLocality() + ", " : ""; //res=(num1>num2) ? (num1+num2):(num1-num2)
            String stateName = (addresses.get(0).getLocality() != null) ? addresses.get(0).getLocality() : "";
            String countryName = addresses.get(0).getCountryName();


            ((PlacesApiViewHolder) holder).placeName.setText(placesArrayList.get(position).getName());
            ((PlacesApiViewHolder) holder).placesLocation.setText(cityName + stateName);
            db.collection(Credentials.USER).document(auth.getCurrentUser().getUid()).collection(Credentials.WISH).document(placesArrayList.get(position).getPlaceId()).get().addOnCompleteListener(task -> {
                if (task.getResult().exists()) {
                    ((PlacesApiViewHolder) holder).wishListAnimation.setMaxFrame(100);
                    ((PlacesApiViewHolder) holder).wishListAnimation.setFrame(51);
                } else {
                    ((PlacesApiViewHolder) holder).wishListAnimation.setMaxFrame(51);


                }
            });
            ((PlacesApiViewHolder) holder).wishListAnimation.setOnClickListener(v -> {
                ((PlacesApiViewHolder) holder).wishListAnimation.playAnimation();
                wishLists.onPlaceClick(placesArrayList.get(position));
            });

        } catch (Exception e) {
            Log.e("ADAPTER", "onBindViewHolder: ", e);
            e.printStackTrace();
        }

        ((PlacesApiViewHolder) holder).places_item_main.setOnClickListener(view -> {
            placesDetails.onClickPlace(placesArrayList.get(position).getPlaceId());
        });

    }

    @Override
    public int getItemCount() {
        return placesArrayList.size();
    }

    public static class PlacesApiViewHolder extends RecyclerView.ViewHolder {

        ImageView placeImage;
        TextView placeName, placesLocation;
        LottieAnimationView wishListAnimation;
        MaterialCardView places_item_main;

        public PlacesApiViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.places_image);
            placeName = itemView.findViewById(R.id.place_name);
            placesLocation = itemView.findViewById(R.id.place_location);
            wishListAnimation = itemView.findViewById(R.id.wishListAnimation);
            places_item_main = itemView.findViewById(R.id.places_item_main);

        }
    }
}

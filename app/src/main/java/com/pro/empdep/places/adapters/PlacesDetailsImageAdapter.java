package com.pro.empdep.places.adapters;

import static com.pro.empdep.places.constants.AppConstant.API_KEY_PHOTO_REF;
import static com.pro.empdep.places.constants.AppConstant.BASE_URL;
import static com.pro.empdep.places.constants.AppConstant.PHOTO_REF;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.pro.empdep.R;

import java.util.ArrayList;

public class PlacesDetailsImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<Bitmap> imageList;
    Context context;

    public PlacesDetailsImageAdapter(ArrayList<Bitmap> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Glide.with(context)
                .load(imageList.get(position))
                .transform(new FitCenter(), new RoundedCorners(28))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(((ImageViewHolder) holder).image);

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.places_image);
        }
    }
}

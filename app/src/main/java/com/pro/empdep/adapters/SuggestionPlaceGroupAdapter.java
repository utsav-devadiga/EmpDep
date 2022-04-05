package com.pro.empdep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.pro.empdep.R;
import com.pro.empdep.model.Group;
import com.pro.empdep.places.interfaces.SuggestedPlaceToGroup;

import java.util.List;

public class SuggestionPlaceGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Group> groupList;
    Context context;
    SuggestedPlaceToGroup suggestedPlaceToGroup;
    String placeid;


    public SuggestionPlaceGroupAdapter(List<Group> groupList, Context context, SuggestedPlaceToGroup suggestedPlaceToGroup, String placeid) {
        this.groupList = groupList;
        this.context = context;
        this.suggestedPlaceToGroup = suggestedPlaceToGroup;
        this.placeid = placeid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);

        return new PlaceSuggestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PlaceSuggestViewHolder) holder).groupName.setText(groupList.get(position).getGroup_name());

        Glide.with(context)
                .load(groupList.get(position).getGroup_picture())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(((PlaceSuggestViewHolder) holder).groupImage);

        ((PlaceSuggestViewHolder) holder).group_item.setOnClickListener(view -> {
            suggestedPlaceToGroup.onGroupSelected(groupList.get(position).getGroup_id(),placeid);
        });
    }


    @Override
    public int getItemCount() {
        return groupList.size();
    }


    public static class PlaceSuggestViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        ImageView groupImage;
        RelativeLayout group_item;

        public PlaceSuggestViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_item_name);
            groupImage = itemView.findViewById(R.id.group_item_image);
            group_item = itemView.findViewById(R.id.group_item_layout);
        }
    }
}

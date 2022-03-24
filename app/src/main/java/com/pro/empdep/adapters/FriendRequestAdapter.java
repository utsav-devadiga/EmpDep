package com.pro.empdep.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.pro.empdep.R;

public class FriendRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_friend_item_accept_reject, parent, false);

        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        TextView username, mutual;
        ImageView profilePic;
        MaterialCardView add, no;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.friend_user_name);
            mutual = itemView.findViewById(R.id.friend_mutual);
            profilePic = itemView.findViewById(R.id.friend_image);
            add = itemView.findViewById(R.id.add_friend_accepted);
            no = itemView.findViewById(R.id.add_friend_rejected);
        }
    }
}

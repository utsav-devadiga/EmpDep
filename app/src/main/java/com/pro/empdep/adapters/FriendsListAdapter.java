package com.pro.empdep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.R;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.interfaces.FriendSelected;
import com.pro.empdep.model.User;

import java.util.ArrayList;
import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<String> userList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FriendSelected friendSelected;

    public FriendsListAdapter(Context context, List<String> userList,FriendSelected friendSelected) {
        this.context = context;
        this.userList = userList;
        this.friendSelected = friendSelected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FriendsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        db.collection(Credentials.USER).document(userList.get(position)).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                User user = task.getResult().toObject(User.class);
                ((FriendsListViewHolder) holder).username.setText(user.getUserName());

                Glide.with(context)
                        .load(user.getPhotoUrl())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(((FriendsListViewHolder) holder).profilePic);

                ((FriendsListViewHolder) holder).selector.setOnClickListener(view -> {
                    friendSelected.onFriendSelected(user.getId());
                });
            }
        });

    }


    @Override
    public int getItemCount() {

        return userList.size();

    }


    public static class FriendsListViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView profilePic;
        CheckBox selector;

        public FriendsListViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.friend_item_name);
            profilePic = itemView.findViewById(R.id.friend_item_image);
            selector = itemView.findViewById(R.id.friend_selector);
        }
    }
}

package com.pro.empdep.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.R;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.User;
import com.pro.empdep.repo.FriendsRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> users;
    Context context;
    FriendsRepository friendsRepository;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    User user;
    User CurrentUser;


    public FriendRequestAdapter(List<String> users, Context context, User currentUser) {
        this.users = users;
        this.context = context;
        friendsRepository = new FriendsRepository();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        CurrentUser = currentUser;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_friend_item_accept_reject, parent, false);

        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        db.collection(Credentials.USER).document(users.get(position)).get().addOnCompleteListener(task -> {
            user = task.getResult().toObject(User.class);


            ArrayList<String> mutualListCurrent = (ArrayList<String>) CurrentUser.getFriends();
            ArrayList<String> mutualList = (ArrayList<String>) user.getFriends();
            mutualListCurrent.retainAll(mutualList);


            if (mutualListCurrent.size() > 0) {
                if (mutualListCurrent.size() == 1) {
                    ((FriendRequestViewHolder) holder).mutual.setText("" + mutualListCurrent.size() + " mutual friend");
                } else {
                    ((FriendRequestViewHolder) holder).mutual.setText("" + mutualListCurrent.size() + " mutual friends");
                }
            }

            if (user != null) {
                ((FriendRequestViewHolder) holder).username.setText(user.getUserName());
                Glide.with(context)
                        .load(user.getPhotoUrl())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(((FriendRequestViewHolder) holder).profilePic);

                ((FriendRequestViewHolder) holder).add.setOnClickListener(view -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(Credentials.USER).document(CurrentUser.getId()).update("friends", FieldValue.arrayUnion(user.getId()));
                    db.collection(Credentials.USER).document(user.getId()).update("friends", FieldValue.arrayUnion(CurrentUser.getId()));
                    db.collection(Credentials.USER).document(CurrentUser.getId()).update("friendReq", FieldValue.arrayRemove(user.getId()));
                    removeItem(users.get(position), position);
                });

                ((FriendRequestViewHolder) holder).no.setOnClickListener(view -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(Credentials.USER).document(CurrentUser.getId()).update("friendReq", FieldValue.arrayRemove(user.getId()));
                    removeItem(users.get(position), position);
                });
            } else {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        if (users != null) {
            return users.size();
        } else {
            return 0;
        }
    }


    public void removeItem(String id, int position) {

        users.remove(id);
        notifyItemRemoved(position);

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

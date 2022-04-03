package com.pro.empdep.adapters;

import android.content.Context;
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
import com.pro.empdep.model.Group;

import java.util.List;

public class GroupRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> groupReqList;
    Context context;
    View view;
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    public GroupRequestAdapter(List<String> groupReqList, Context context) {
        this.groupReqList = groupReqList;
        this.context = context;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_request_item, parent, false);
        return new GroupReqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        db.collection(Credentials.GROUP).document(groupReqList.get(position)).get().addOnCompleteListener(task -> {
            Group group = task.getResult().toObject(Group.class);
            Log.d("GROUP-REQUEST-ADAPTER", "onBindViewHolder: " + group);

            ((GroupReqViewHolder) holder).username.setText(group.getGroup_name());

            Glide.with(context)
                    .load(group.getGroup_picture())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(((GroupReqViewHolder) holder).profilePic);

            ((GroupReqViewHolder) holder).loading.setVisibility(View.GONE);

            if (group.getUsers() != null) {
                ((GroupReqViewHolder) holder).mutual.setText("" + group.getUsers().size() + " users");
            }

            ((GroupReqViewHolder) holder).add.setOnClickListener(v -> {
                // add the users into group and remove the request
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(Credentials.USER).document(mAuth.getCurrentUser().getUid()).update("groups", FieldValue.arrayUnion(groupReqList.get(position)));
                db.collection(Credentials.USER).document(mAuth.getCurrentUser().getUid()).update("groupReq", FieldValue.arrayRemove(groupReqList.get(position)));
                db.collection(Credentials.GROUP).document(groupReqList.get(position)).update("users", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid()));
                db.collection(Credentials.GROUP).document(groupReqList.get(position)).update("pendingRequest", FieldValue.arrayRemove(mAuth.getCurrentUser().getUid()));
                db.collection(Credentials.GROUP).document(groupReqList.get(position)).update("pendingSentRequest", FieldValue.arrayRemove(mAuth.getCurrentUser().getUid()));
                removeItem(groupReqList.get(position), position);
            });

            ((GroupReqViewHolder) holder).no.setOnClickListener(v -> {
                // remove the request from group pending and the user
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(Credentials.USER).document(mAuth.getCurrentUser().getUid()).update("groupReq", FieldValue.arrayRemove(groupReqList.get(position)));
                db.collection(Credentials.GROUP).document(groupReqList.get(position)).update("pendingRequest", FieldValue.arrayRemove(mAuth.getCurrentUser().getUid()));
                db.collection(Credentials.GROUP).document(groupReqList.get(position)).update("pendingSentRequest", FieldValue.arrayRemove(mAuth.getCurrentUser().getUid()));
                removeItem(groupReqList.get(position), position);
            });

        });

    }

    public void removeItem(String id, int position) {

        groupReqList.remove(id);
        notifyItemRemoved(position);

    }

    @Override
    public int getItemCount() {

        if (groupReqList != null) {
            return groupReqList.size();
        } else {
            return 0;
        }
    }

    public static class GroupReqViewHolder extends RecyclerView.ViewHolder {
        TextView username, mutual;
        ImageView profilePic;
        LottieAnimationView loading;
        MaterialCardView add, no;

        public GroupReqViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.group_user_name);
            mutual = itemView.findViewById(R.id.group_mutual);
            profilePic = itemView.findViewById(R.id.group_image);
            add = itemView.findViewById(R.id.add_group_accepted);
            no = itemView.findViewById(R.id.add_group_rejected);
            loading = itemView.findViewById(R.id.loadingAnimation);

        }
    }
}


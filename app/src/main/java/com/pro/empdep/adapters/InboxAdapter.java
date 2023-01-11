package com.pro.empdep.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.R;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.interfaces.MessageOpener;
import com.pro.empdep.model.Group;
import com.pro.empdep.model.Message;
import com.pro.empdep.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InboxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Group> inboxList;
    View view;
    Context context;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    MessageOpener messageOpener;

    public InboxAdapter(List<Group> inboxList, Context context, MessageOpener messageOpener) {
        this.inboxList = inboxList;
        this.context = context;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        this.messageOpener = messageOpener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox, parent, false);
        return new InboxViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (!inboxList.isEmpty()) {
            ((InboxViewHolder) holder).groupName.setText(inboxList.get(position).getGroup_name());

            Glide.with(context)
                    .load(inboxList.get(position).getGroup_picture())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(((InboxViewHolder) holder).groupPic);

//        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
            ((InboxViewHolder) holder).timestamp.setText(getDate(Long.parseLong(inboxList.get(position).getTimestamp())));

            ((InboxViewHolder) holder).message_item.setOnClickListener(v -> {
                messageOpener.onGroupClick(inboxList.get(position).getGroup_id());
            });
            if (inboxList.get(position).getLast_message_sent_by() != null && !inboxList.get(position).getLast_message_sent_by().equals("")) {
                db.collection(Credentials.USER).document(inboxList.get(position).getLast_message_sent_by()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = task.getResult().toObject(User.class);
                        if (user != null) {
                            if (user.getId().equalsIgnoreCase(mAuth.getCurrentUser().getUid())) {
                                ((InboxViewHolder) holder).lastMessage.setText("You: " + inboxList.get(position).getLast_message());
                            } else {
                                ((InboxViewHolder) holder).lastMessage.setText(user.getUserName() + ": " + inboxList.get(position).getLast_message());
                            }

                        } else {
                            ((InboxViewHolder) holder).lastMessage.setText(inboxList.get(position).getLast_message());
                        }

                    }
                });
            } else {
                ((InboxViewHolder) holder).lastMessage.setText(inboxList.get(position).getLast_message());
            }

            if (inboxList.get(position).getSeen_by() != null && inboxList.get(position).getSeen_by().contains(mAuth.getCurrentUser().getUid())) {
                ((InboxViewHolder) holder).read.setVisibility(View.GONE);
            } else {
                ((InboxViewHolder) holder).read.setVisibility(View.VISIBLE);
            }
        }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        Calendar now = Calendar.getInstance();
        cal.setTimeInMillis(time);

        if (cal.get(Calendar.DAY_OF_YEAR) < now.get(Calendar.DAY_OF_YEAR)) {
            String date = DateFormat.format("dd/MM/yy", cal).toString();
            return date;
        } else {
            String date = DateFormat.format("hh:mm", cal).toString();
            return date;
        }

    }

    @Override
    public int getItemCount() {
        return inboxList.size();
    }


    public static class InboxViewHolder extends RecyclerView.ViewHolder {

        ImageView groupPic;
        RelativeLayout message_item;
        TextView groupName, lastMessage, timestamp;
        ImageView read;


        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            groupPic = itemView.findViewById(R.id.inbox_user_image);
            groupName = itemView.findViewById(R.id.inbox_user_header);
            lastMessage = itemView.findViewById(R.id.lastText);
            timestamp = itemView.findViewById(R.id.inbox_user_time);
            message_item = itemView.findViewById(R.id.inbox_conversation);
            read = itemView.findViewById(R.id.user_read);
        }
    }

}

package com.pro.empdep.repo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.JsonObject;
import com.pro.empdep.apiClients.FriendRequest;
import com.pro.empdep.apiClients.FriendRequestClient;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.firebase.RandomPhotoUrlGenerator;
import com.pro.empdep.model.Group;
import com.pro.empdep.model.GroupValidator;
import com.pro.empdep.model.NotificationBody;
import com.pro.empdep.model.NotificationContent;
import com.pro.empdep.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupRepository {


    FirebaseFirestore db;
    public static String TAG = "USER-REPO";
    FirebaseAuth auth;
    FriendRequestClient friendRequestClient;


    public GroupRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        friendRequestClient = FriendRequest.getRetrofitInstance().create(FriendRequestClient.class);
    }


    public LiveData<Boolean> groupCreate(ArrayList<String> groupMembers, String groupName) {
        final MutableLiveData<Boolean> GROUP_STATUS = new MutableLiveData<>();
        GROUP_STATUS.setValue(true);
        String currentUserId = auth.getCurrentUser().getUid();
        String groupId = db.collection(Credentials.GROUP).document().getId();
        RandomPhotoUrlGenerator randomPhotoUrlGenerator = new RandomPhotoUrlGenerator();
        String groupPic = "https://avatars.dicebear.com/api/identicon/" + (new RandomPhotoUrlGenerator().randomUsernameExtension()) + ".png?colors=teal";
        ArrayList<String> userList = new ArrayList<>();
        userList.add(auth.getCurrentUser().getUid());
        Group group = new Group(groupId, 0, currentUserId, "NO-MESSAGES", "NO-MESSAGES", new ArrayList<String>(),userList , groupMembers, groupMembers, String.valueOf(System.currentTimeMillis()), groupPic, groupName, currentUserId);
        db.collection(Credentials.GROUP).document(groupId).set(group).addOnCompleteListener(task -> {
            //need to add the an array of group names so that while creating groups we can validate
            if (task.isSuccessful()) {
                GROUP_STATUS.setValue(true);

                db.collection(Credentials.GROUP).document(Credentials.GROUP_VALIDATOR).update(Credentials.GROUP_VALIDATOR, FieldValue.arrayUnion(groupName)).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        for (String user : groupMembers) {
                            //gets device-id and sends notifications
                            db.collection(Credentials.USER).document(user).get().addOnCompleteListener(notificationTask -> {

                                if (notificationTask.isSuccessful()) {

                                    User userNotification = notificationTask.getResult().toObject(User.class);
                                    String deviceToken = userNotification.getDevice();

                                    NotificationBody notificationBody = new NotificationBody(deviceToken, new NotificationContent(auth.getCurrentUser().getDisplayName() + " invited you to join " + groupName, ""));
                                    friendRequestClient.sendFriendRequestNotification(notificationBody).enqueue(new Callback<JsonObject>() {
                                        @Override
                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                            Log.d(TAG, "onResponse: notification sent " + response);
                                        }

                                        @Override
                                        public void onFailure(Call<JsonObject> call, Throwable t) {
                                            Log.d(TAG, "onResponse: notification not sent" + t.getMessage());
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "onResponse: notification not sent");
                                }


                            });
                            //adds group invite to users node
                            db.collection(Credentials.USER).document(user).update("groupReq", FieldValue.arrayUnion(groupId));



                        }
                        //adds self too
                        db.collection(Credentials.USER).document(auth.getCurrentUser().getUid()).update("groups",FieldValue.arrayUnion(groupId));
                        GROUP_STATUS.setValue(true);
                    } else {
                        GROUP_STATUS.setValue(false);
                    }
                });
            } else {
                GROUP_STATUS.setValue(false);
            }
        });

        return GROUP_STATUS;
    }


    public LiveData<Boolean> validGroupName(String groupName) {
        final MutableLiveData<Boolean> GROUP_VALIDATION = new MutableLiveData<>();
        final MutableLiveData<GroupValidator> groupNameList = new MutableLiveData<>();
        db.collection(Credentials.GROUP).document(Credentials.GROUP_VALIDATOR).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                groupNameList.setValue(task.getResult().toObject(GroupValidator.class));

                try {
                    if (groupNameList.getValue().getGROUP_VALIDATOR().contains(groupName)) {
                        GROUP_VALIDATION.setValue(false);
                    } else {
                        GROUP_VALIDATION.setValue(true);
                    }
                } catch (Exception e) {
                    GROUP_VALIDATION.setValue(true);
                }
            } else {
                GROUP_VALIDATION.setValue(false);
            }
        });


        return GROUP_VALIDATION;
    }

    public LiveData<List<String>> getPendingRequest() {
        final MutableLiveData<List<String>> pendingRequestList = new MutableLiveData<>();

        db.collection(Credentials.USER).document(auth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
            if (error == null) {

                if (value != null) {

                    User user = value.toObject(User.class);
                    pendingRequestList.setValue(user.getGroupReq());

                } else {
                    pendingRequestList.setValue(null);
                }

            } else {
                //error
                pendingRequestList.setValue(null);
            }
        });

        return pendingRequestList;
    }

}


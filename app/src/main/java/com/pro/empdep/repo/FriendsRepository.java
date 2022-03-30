package com.pro.empdep.repo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.User;
import com.pro.empdep.places.response.PlacesResponse;

import java.util.ArrayList;
import java.util.List;

public class FriendsRepository {

    FirebaseFirestore db;
    public static String TAG = "FRIENDS-REPO";
    FirebaseAuth auth;

    public FriendsRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public LiveData<List<User>> getAllUsersFromServer() {
        final MutableLiveData<List<User>> allUsers = new MutableLiveData<>();
        db.collection(Credentials.USER).addSnapshotListener((value, error) -> {
            if (error != null) {
                allUsers.setValue(null);
            } else {
                if (value != null) {
                    try {
                        allUsers.setValue(value.toObjects(User.class));
                    } catch (Exception e) {
                        Log.d(TAG, "getAllUsersFromServer: " + e.getMessage());
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        return allUsers;
    }

    public LiveData<List<String>> getPendingFriendRequest() {
        final MutableLiveData<List<String>> pendingRequest = new MutableLiveData<>();
        db.collection(Credentials.USER).document(auth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
            if (error != null) {
                pendingRequest.setValue(null);
            } else {
                if (value != null) {
                    pendingRequest.setValue(value.toObject(User.class).getFriendReq());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
        return pendingRequest;
    }


    public MutableLiveData<User> getCurrentUser() {
        final MutableLiveData<User> user = new MutableLiveData<>();
        db.collection(Credentials.USER).document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "getSingleUser: " + task.getResult().toObject(User.class));
                user.setValue(task.getResult().toObject(User.class));
            } else {
                user.setValue(null);
            }
        });
        return user;
    }


    public LiveData<User> getUsersData(String id) {
        final MutableLiveData<User> usersData = new MutableLiveData<>();
        db.collection(Credentials.USER).document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "getSingleUser: " + task.getResult().toObject(User.class));
                usersData.setValue(task.getResult().toObject(User.class));
            } else {
                usersData.setValue(null);
            }
        });
        return usersData;
    }



    public LiveData<List<String>> getAllFriends() {
        final MutableLiveData<List<String>> friendsListID = new MutableLiveData<>();

        db.collection(Credentials.USER).document(auth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {

            if (error != null) {
                friendsListID.setValue(null);
            } else {
                friendsListID.setValue(value.toObject(User.class).getFriends());

            }
        });

        return friendsListID;
    }

}

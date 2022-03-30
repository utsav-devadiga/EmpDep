package com.pro.empdep.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pro.empdep.model.User;
import com.pro.empdep.repo.FriendsRepository;

import java.util.List;


public class FriendsViewModel extends AndroidViewModel {

    FriendsRepository friendsRepository;
    private LiveData<List<User>> allUsersLiveData;
    private LiveData<List<String>> pendingFriendRequest;
    private LiveData<User> currentUser;
    private LiveData<List<String>> friends;

    public FriendsViewModel(@NonNull Application application) {
        super(application);
        friendsRepository = new FriendsRepository();
        this.allUsersLiveData = friendsRepository.getAllUsersFromServer();
        this.pendingFriendRequest = friendsRepository.getPendingFriendRequest();
        this.currentUser = friendsRepository.getCurrentUser();
        this.friends = friendsRepository.getAllFriends();
    }

    public LiveData<List<User>> getAllUsersLiveData() {
        return allUsersLiveData;
    }


    public LiveData<List<String>> getPendingFriendRequest() {
        return pendingFriendRequest;
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<List<String>> getFriends() {
        return friends;
    }

}

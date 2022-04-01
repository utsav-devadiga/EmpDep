package com.pro.empdep.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pro.empdep.model.FriendList;
import com.pro.empdep.repo.GroupRepository;

import java.util.ArrayList;

public class GroupViewModel extends AndroidViewModel {
    GroupRepository groupRepository;
    private LiveData<Boolean> groupCreatedData;

    private  MutableLiveData<FriendList> friends ;



    public GroupViewModel(@NonNull Application application) {
        super(application);
        groupRepository = new GroupRepository();
    }


    public LiveData<Boolean> groupCreated(ArrayList<String> groupMembers, String groupName) {
        return groupRepository.groupCreate(groupMembers, groupName);
    }

    public LiveData<Boolean> validGroupName(String groupName) {
        return groupRepository.validGroupName(groupName);
    }

    public MutableLiveData<FriendList> getFriends() {
        return friends;
    }

    public void setFriends(MutableLiveData<FriendList> friends) {
        this.friends = friends;
    }
}

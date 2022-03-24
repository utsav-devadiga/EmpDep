package com.pro.empdep.viewmodel;

import static com.pro.empdep.places.constants.AppConstant.API_KEY;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pro.empdep.model.User;
import com.pro.empdep.places.response.PlacesResponse;
import com.pro.empdep.repo.FriendsRepository;

import java.util.List;


public class FriendsViewModel extends AndroidViewModel {

    FriendsRepository friendsRepository;
    private LiveData<List<User>> allUsersLiveData;

    public FriendsViewModel(@NonNull Application application) {
        super(application);
        friendsRepository = new FriendsRepository();
        this.allUsersLiveData = friendsRepository.getAllUsersFromServer();
    }

    public LiveData<List<User>> getAllUsersLiveData() {
        return allUsersLiveData;
    }
}

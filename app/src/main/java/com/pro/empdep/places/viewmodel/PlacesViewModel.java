package com.pro.empdep.places.viewmodel;

import static com.pro.empdep.places.constants.AppConstant.API_KEY;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pro.empdep.places.constants.AppConstant;
import com.pro.empdep.places.repo.PlacesRepo;
import com.pro.empdep.places.response.PlacesResponse;

public class PlacesViewModel extends AndroidViewModel {

    private PlacesRepo placesRepo;
    private LiveData<PlacesResponse> placesResponseLiveData;
    private LiveData<PlacesResponse> thingsToDoResponseLiveData;

    public PlacesViewModel(@NonNull Application application) {
        super(application);
        placesRepo = new PlacesRepo();
    }

    public LiveData<PlacesResponse> getPlacesResponseLiveData(String query) {
        return placesRepo.getPlaces(query, API_KEY);
    }

    public LiveData<PlacesResponse> getNewPlacesResponseLiveData(String query) {
        return placesRepo.getNewPlaces(query, API_KEY);

    }

    public LiveData<PlacesResponse> getThingsToDoResponseLiveData(String query){
        return placesRepo.getThingsToDoPlaces(query,API_KEY);
    }
}

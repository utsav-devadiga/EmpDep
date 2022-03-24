package com.pro.empdep.places.repo;

import android.media.session.MediaSession;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pro.empdep.places.apiClients.PlacesClient;
import com.pro.empdep.places.apiClients.PlacesRequest;
import com.pro.empdep.places.response.PlacesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesRepo {
    private static final String TAG = PlacesRepo.class.getSimpleName();
    PlacesClient placesRequest;
    String token;


    public PlacesRepo() {
        placesRequest = PlacesRequest.getRetrofitInstance().create(PlacesClient.class);

    }


    public LiveData<PlacesResponse> getPlaces(String query, String key) {
        final MutableLiveData<PlacesResponse> placesData = new MutableLiveData<>();
        placesRequest.getPlaces(query, key)
                .enqueue(new Callback<PlacesResponse>() {
                    @Override
                    public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                        Log.d(TAG, "onResponse response:: " + response);


                        if (response.body() != null) {
                            placesData.setValue(response.body());

                            Log.d(TAG, "places status:: " + response.body().getStatus());
                            Log.d(TAG, "places-list size:: " + response.body().getResults().size());
                            token = response.body().getNextPageToken();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlacesResponse> call, Throwable t) {
                        placesData.setValue(null);
                    }
                });

        return placesData;
    }


    public LiveData<PlacesResponse> getNewPlaces(String query, String key) {

        final MutableLiveData<PlacesResponse> placesData = new MutableLiveData<>();
        if (token != null) {
            placesRequest.getNextPlaces(query, key, token)
                    .enqueue(new Callback<PlacesResponse>() {
                        @Override
                        public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                            Log.d(TAG, "onResponse response:: " + response);


                            if (response.body() != null) {
                                placesData.setValue(response.body());

                                Log.d(TAG, "places status:: " + response.body().getStatus());
                                Log.d(TAG, "places-list size:: " + response.body().getResults().size());
                                if (response.body().nextPageToken != null) {
                                    token = response.body().getNextPageToken();
                                } else {
                                    token = null;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<PlacesResponse> call, Throwable t) {
                            placesData.setValue(null);
                        }
                    });
        } else {
            placesData.setValue(null);
        }

        return placesData;
    }

}

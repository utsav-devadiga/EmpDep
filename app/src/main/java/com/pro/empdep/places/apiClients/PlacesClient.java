package com.pro.empdep.places.apiClients;

import com.pro.empdep.places.response.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesClient {

    @GET("maps/api/place/textsearch/json")
    Call<PlacesResponse> getPlaces(
            @Query(value = "query", encoded = true) String query,
            @Query("key") String apiKey
    );

    @GET("maps/api/place/textsearch/json")
    Call<PlacesResponse> getNextPlaces(
            @Query(value = "query", encoded = true) String query,
            @Query("key") String apiKey,
            @Query("pagetoken") String nextPagToken
    );


}

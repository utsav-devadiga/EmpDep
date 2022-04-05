package com.pro.empdep.screens;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pro.empdep.R;
import com.pro.empdep.databinding.FragmentPlacesDetailsBinding;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.places.adapters.PlacesDetailsImageAdapter;
import com.pro.empdep.places.constants.AppConstant;
import com.pro.empdep.places.interfaces.PlacesDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlacesDetailsFragment extends Fragment {
    FragmentPlacesDetailsBinding binding;
    View view;
    String placeId = "";
    ArrayList<Bitmap> placesImage = new ArrayList<>();
    PlacesDetailsImageAdapter adapter;


    public PlacesDetailsFragment() {
        // Required empty public constructor
    }

    public static PlacesDetailsFragment newInstance(String param1, String param2) {
        PlacesDetailsFragment fragment = new PlacesDetailsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPlacesDetailsBinding.inflate(inflater, container, false);
        // getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(),R.color.transculent));

        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        placeId = PlacesDetailsFragmentArgs.fromBundle(getArguments()).getPlaceId();


        // Specify the fields to return.
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.ADDRESS, Place.Field.OPENING_HOURS);

        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        // Initialize Places.
        Places.initialize(requireContext(), AppConstant.API_KEY);

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(requireContext());
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i("PLACES", "Place found: " + place.getId());
            if (place.getOpeningHours() != null) {
                Log.i("PLACES", "Place found: " + place.getOpeningHours().getWeekdayText().get(0));
            }
            Log.i("PLACES", "Place found: " + place.getAddress());
            binding.placesDetailsTitle.setText(place.getName());
            // Get the photo metadata.
            final List<PhotoMetadata> metadata = place.getPhotoMetadatas();

            if (metadata == null || metadata.isEmpty()) {
                Log.w("PLACES", "No photo metadata.");
            }
            placesImage.clear();
            for (PhotoMetadata meta : metadata
            ) {
                final PhotoMetadata photoMetadata = meta;

                // Get the attribution text.
                final String attributions = photoMetadata.getAttributions();

                // Create a FetchPhotoRequest.
                final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    placesImage.add(bitmap);
                    configureViewPager();
                });
            }

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e("PLACES", "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
                // TODO: Handle error with given status code.
            }
        });


    }

    private void configureViewPager() {
        Log.d("PLACES", "configureViewPager: " + placesImage.size());
        adapter = new PlacesDetailsImageAdapter(placesImage, getContext());
        binding.placesImagesViewPager.setAdapter(adapter);
        //indicator
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.viewPagerIndicator, binding.placesImagesViewPager, true, true, (tab, position) -> {
            //something to do that i am not sure of
        });
        tabLayoutMediator.attach();

    }

    @Override
    public void onPause() {
        super.onPause();
        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.primary));
    }

    @Override
    public void onStop() {
        super.onStop();
        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.primary));
    }
}
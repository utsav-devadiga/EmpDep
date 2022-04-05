package com.pro.empdep.screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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
import com.pro.empdep.places.adapters.OpeningHourAdapter;
import com.pro.empdep.places.adapters.PlacesApiAdapter;
import com.pro.empdep.places.adapters.PlacesDetailsImageAdapter;
import com.pro.empdep.places.constants.AppConstant;
import com.pro.empdep.places.interfaces.PlacesDetails;
import com.pro.empdep.places.model.PlacesModel;
import com.pro.empdep.places.repo.PlacesRepo;
import com.pro.empdep.places.viewmodel.PlacesViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlacesDetailsFragment extends Fragment implements PlacesDetails {
    FragmentPlacesDetailsBinding binding;
    View view;
    String placeId = "";
    String placeName = "";
    ArrayList<Bitmap> placesImage = new ArrayList<>();
    PlacesDetailsImageAdapter adapter;
    PlacesViewModel viewModel;
    NavController navController;

    ArrayList<PlacesModel> placesArrayList = new ArrayList<>();
    PlacesApiAdapter placesApiAdapter;


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




        viewModel = new ViewModelProvider(this).get(PlacesViewModel.class);

        view = binding.getRoot();


        binding.suggestBtn.setOnClickListener(view -> {

            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Credentials.GROUP, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("place", placeId);
            editor.apply();

            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            navController = navHostFragment.getNavController();
            NavDirections actions = PlacesDetailsFragmentDirections.actionPlacesDetailsFragmentToGroupSelectionBottomSheet();

            navController.navigate(actions);

        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        placeId = PlacesDetailsFragmentArgs.fromBundle(getArguments()).getPlaceId();


        // Specify the fields to return.
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.ADDRESS, Place.Field.OPENING_HOURS, Place.Field.RATING, Place.Field.USER_RATINGS_TOTAL);

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
                OpeningHourAdapter adapterHour = new OpeningHourAdapter(place.getOpeningHours().getWeekdayText(), getContext());
                binding.openingHoursCycle.setAdapter(adapterHour);

                binding.openingHoursLayout.setVisibility(View.VISIBLE);
            }
            Log.i("PLACES", "Place found: " + place.getAddress());
            binding.totalUserRating.setText("(" + String.valueOf(place.getUserRatingsTotal()) + ")");
            placeName = place.getName();
            binding.placesDetailsTitle.setText(place.getName());
            binding.placesDetailsAddress.setText(place.getAddress());
            binding.placesDetailRatings.setRating(Float.parseFloat(String.valueOf(place.getRating())));
            placesApiAdapter = new PlacesApiAdapter(getContext(), placesArrayList, this);
            binding.thingsToDoCycle.setAdapter(placesApiAdapter);
            binding.thingsToDoHeader.setText(AppConstant.THINGS_TODO_QUERY + placeName);
            viewModel.getThingsToDoResponseLiveData(AppConstant.THINGS_TODO_QUERY + placeName).observe(getViewLifecycleOwner(), places -> {
                List<PlacesModel> placesList = places.getResults();
                placesArrayList.addAll(placesList);
                placesApiAdapter.notifyDataSetChanged();
            });
            // Get the photo metadata.
            final List<PhotoMetadata> metadata = place.getPhotoMetadatas();

            if (metadata == null || metadata.isEmpty()) {
                Log.w("PLACES", "No photo metadata.");
            }
            placesImage.clear();
            for (PhotoMetadata meta : metadata) {
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

    @Override
    public void onClickPlace(String place_id) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavDirections actions = PlacesDetailsFragmentDirections.actionPlacesDetailsFragmentSelf(place_id);

        navController.navigate(actions);
    }
}
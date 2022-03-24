package com.pro.empdep.screens;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.R;
import com.pro.empdep.account_screens.HomeActivity;
import com.pro.empdep.adapters.PlaceAdapter;
import com.pro.empdep.databinding.FragmentHomeBinding;
import com.pro.empdep.model.Places;
import com.pro.empdep.places.adapters.PlacesApiAdapter;
import com.pro.empdep.places.model.PlacesModel;
import com.pro.empdep.places.viewmodel.PlacesViewModel;
import com.pro.empdep.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    View v;
    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView placesRecyclerView, placesApiRecyclerview;
    NavController navController;
    UserViewModel userViewModel;
    PlacesViewModel placesViewModel;
    ArrayList<PlacesModel> placesArrayList = new ArrayList<>();
    PlacesApiAdapter placesApiAdapter;
    ScrollView mainViewScrollview;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //when you have data to pass
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);

        mainViewScrollview = v.findViewById(R.id.MainViewScrollview);

        placesRecyclerView = v.findViewById(R.id.places_cycle);
        placesApiRecyclerview = v.findViewById(R.id.api_places_cycle);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);


        List<Places> placesList = new ArrayList<>();
        placesList.add(new Places("Taj Mahal", R.drawable.place_1, "₹2,500", "4.5", "Agra,India"));
        placesList.add(new Places("Beaches", R.drawable.place, "₹500", "4.7", "Goa,India"));
        placesList.add(new Places("Sub-Urban", R.drawable.city, "₹500", "4.0", "Mumbai,India"));
        placesList.add(new Places("Hills", R.drawable.hills, "₹12,500", "5.0", "India"));
        placesList.add(new Places("Forest", R.drawable.forest, "₹1,500", "4.5", "Pune,India"));

        PlaceAdapter adapter = new PlaceAdapter(placesList);
        placesRecyclerView.setAdapter(adapter);

        placesApiAdapter = new PlacesApiAdapter(getContext(), placesArrayList);
        placesApiRecyclerview.setHasFixedSize(true);
        placesApiRecyclerview.setAdapter(placesApiAdapter);
        getPlaces();


        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        userViewModel.checkUserExist(v);


        placesApiRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!placesApiRecyclerview.canScrollHorizontally(1)) {
                    getNewPlaces();
                }
            }


        });



        return v;

    }

    private void getPlaces() {

        placesViewModel.getPlacesResponseLiveData().observe(getViewLifecycleOwner(), placesResponse -> {
            if (placesResponse != null) {
                //TODO Shimmer loading

                List<PlacesModel> placesList = placesResponse.getResults();
                placesArrayList.addAll(placesList);
                placesApiAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getNewPlaces() {
        placesViewModel.getNewPlacesResponseLiveData().observe(getViewLifecycleOwner(), placesResponse -> {
            if (placesResponse != null) {
                //TODO Shimmer loading

                List<PlacesModel> placesList = placesResponse.getResults();
                placesArrayList.addAll(placesList);
                placesApiAdapter.notifyDataSetChanged();
            }
        });
    }

}
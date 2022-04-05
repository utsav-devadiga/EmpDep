package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pro.empdep.R;
import com.pro.empdep.databinding.FragmentWishListBinding;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.places.adapters.WishListAdapters;
import com.pro.empdep.places.interfaces.PlacesDetails;
import com.pro.empdep.places.model.PlacesModel;

import java.util.ArrayList;


public class WishListFragment extends Fragment implements PlacesDetails {

    View view;
    FragmentWishListBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<PlacesModel> placesList = new ArrayList<>();
    NavController navController;
    WishListAdapters adapter;

    public WishListFragment() {
        // Required empty public constructor
    }


    public static WishListFragment newInstance(String param1, String param2) {
        WishListFragment fragment = new WishListFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentWishListBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        adapter = new WishListAdapters(getContext(), placesList, this);
        binding.wishlistCycle.setAdapter(adapter);

        db.collection(Credentials.USER).document(auth.getCurrentUser().getUid()).collection(Credentials.WISH).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                placesList.clear();
                placesList.addAll(task.getResult().toObjects(PlacesModel.class));
                adapter.notifyDataSetChanged();
            }
        });

        //handle navigation
        backPress();
        return view;
    }

    private void backPress() {

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        callback.setEnabled(true);


    }

    @Override
    public void onClickPlace(String place_id) {
        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavDirections actions = WishListFragmentDirections.actionWishListFragmentToPlacesDetailsFragment(place_id);
        navController.navigate(actions);
    }
}
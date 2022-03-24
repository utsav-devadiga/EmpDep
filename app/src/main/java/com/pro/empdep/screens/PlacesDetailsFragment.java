package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pro.empdep.R;

public class PlacesDetailsFragment extends Fragment {


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places_details, container, false);
    }
}
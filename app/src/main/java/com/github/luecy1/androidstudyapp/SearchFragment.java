package com.github.luecy1.androidstudyapp;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.luecy1.androidstudyapp.ui.common.NavigationController;

import javax.inject.Inject;


public class SearchFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

//    DataBindingComponent dataBindingComponent = new Fragmen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        FragmentSearchBinding binding DataBindingUtil
//                .inflate(inflater, R.layout.fragment_search, container, false,
//
//                        da)
        return null;
    }
}

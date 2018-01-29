package com.github.luecy1.androidstudyapp.ui.search;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.luecy1.androidstudyapp.R;
import com.github.luecy1.androidstudyapp.binding.FragmentDataBindingComponent;
import com.github.luecy1.androidstudyapp.databinding.FragmentSearchBinding;
import com.github.luecy1.androidstudyapp.di.Injectible;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;
import com.github.luecy1.androidstudyapp.util.AutoClearedValue;

import javax.inject.Inject;


public class SearchFragment extends Fragment implements Injectible {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentSearchBinding> binding;

    private SearchViewModel searchViewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSearchBinding dataBinding =  DataBindingUtil
                .inflate(inflater, R.layout.fragment_search, container, false,
                        dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);

    }
}

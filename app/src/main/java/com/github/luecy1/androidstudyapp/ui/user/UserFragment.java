package com.github.luecy1.androidstudyapp.ui.user;

import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingComponent;
import android.support.v4.app.Fragment;

import com.github.luecy1.androidstudyapp.binding.FragmentDataBindingComponent;
import com.github.luecy1.androidstudyapp.di.Injectible;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;

import javax.inject.Inject;

/**
 * Created by you on 2018/02/07.
 */
// TODO
public class UserFragment extends Fragment implements Injectible {

    private static final String LOGIN_KEY = "login";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

// TODO
}

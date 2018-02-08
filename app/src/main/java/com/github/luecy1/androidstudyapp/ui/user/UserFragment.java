package com.github.luecy1.androidstudyapp.ui.user;

import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.luecy1.androidstudyapp.R;
import com.github.luecy1.androidstudyapp.binding.FragmentDataBindingComponent;
import com.github.luecy1.androidstudyapp.databinding.UserFragmentBinding;
import com.github.luecy1.androidstudyapp.di.Injectible;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;
import com.github.luecy1.androidstudyapp.ui.common.RepoListAdapter;
import com.github.luecy1.androidstudyapp.util.AutoClearedValue;

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

    @VisibleForTesting
    AutoClearedValue<UserFragmentBinding> binding;
    private AutoClearedValue<RepoListAdapter> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UserFragmentBinding databinding = DataBindingUtil.inflate(inflater, R.layout.user_fragment, container,
                false, dataBindingComponent);
        databinding.setRetryCallback(() -> userViewModel.retry());
        binding = new AutoClearedValue<>(this, databinding);
        return databinding.getRoot();
    }

    // TODO
}

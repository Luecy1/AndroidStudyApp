package com.github.luecy1.androidstudyapp.ui.repo;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.luecy1.androidstudyapp.binding.FragmentDataBindingComponent;
import com.github.luecy1.androidstudyapp.di.Injectible;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;

import javax.inject.Inject;

/**
 * Created by you on 2018/01/28.
 */
// TODO
public class RepoFragment extends Fragment implements LifecycleRegistryOwner, Injectible {

    private final static String REPO_OWNER_KEY = "repo_owner";

    private final static String REPO_NAME_KEY = "repo_name";

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RepoViewModel repoViewModel;

    @Inject
    NavigationController navigationController;

    android.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    //   AutoCloseable<>
    //   AutoCloseable<>
    //TODO

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        repoViewModel = ViewModelProviders.of(this, viewModelFactory).get(RepoViewModel.class);
        Bundle args = getArguments();
        if (args != null
                && args.containsKey(REPO_OWNER_KEY)
                && args.containsKey(REPO_NAME_KEY)) {
            repoViewModel.setId(args.getString(REPO_OWNER_KEY), args.getString(REPO_NAME_KEY));
        } else {
            repoViewModel.setId(null, null);
        }
        LiveData<Resource<Repo>> repo = repoViewModel.getRepo();
        // TODO
    }
}

package com.github.luecy1.androidstudyapp.ui.repo;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.Fragment;

import com.github.luecy1.androidstudyapp.di.Injectible;

import javax.inject.Inject;

/**
 * Created by you on 2018/01/28.
 */

public class RepoFragment extends Fragment implements LifecycleRegistryOwner,Injectible {

    private static final String REPO_OWNER_KEY = "repo_owner";

    private static final String REPO_NAME_KEY = "repo_name";

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RepoViewModel repoViewModel;


    @Override
    public LifecycleRegistry getLifecycle() {
        return null;
    }
}

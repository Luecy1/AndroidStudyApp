package com.github.luecy1.androidstudyapp.ui.repo;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
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
import com.github.luecy1.androidstudyapp.databinding.RepoFragmentBinding;
import com.github.luecy1.androidstudyapp.di.Injectable;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;
import com.github.luecy1.androidstudyapp.util.AutoClearedValue;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;

import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by you on 2018/01/28.
 */
public class RepoFragment extends Fragment implements Injectable {

    private final static String REPO_OWNER_KEY = "repo_owner";

    private final static String REPO_NAME_KEY = "repo_name";

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RepoViewModel repoViewModel;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<RepoFragmentBinding> binding;
    AutoClearedValue<ContributorAdapter> adapter;

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
        repo.observe(this, resource -> {
            binding.get().setRepo(resource == null ? null : resource.data);
            binding.get().setRepoResource(resource);
            binding.get().executePendingBindings();
        });

        ContributorAdapter adapter = new ContributorAdapter(dataBindingComponent,
                contributor -> navigationController.navigateToUser(contributor.getLogin()));
        this.adapter = new AutoClearedValue<>(this, adapter);
        binding.get().contributorList.setAdapter(adapter);
        initContributorList(repoViewModel);
    }

    private void initContributorList(RepoViewModel viewModel) {
        viewModel.getContributors().observe(this, listResource -> {

            if (listResource != null && listResource.data != null) {
                adapter.get().replace(listResource.data);
            } else {
                adapter.get().replace(Collections.emptyList());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RepoFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.repo_fragment, container, false);
        dataBinding.setRetryCallback(() -> repoViewModel.retry());
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    public static RepoFragment create(String owner, String name) {
        RepoFragment repoFragment = new RepoFragment();
        Bundle args = new Bundle();
        args.putString(REPO_OWNER_KEY, owner);
        args.putString(REPO_NAME_KEY, name);
        repoFragment.setArguments(args);
        return repoFragment;
    }
}

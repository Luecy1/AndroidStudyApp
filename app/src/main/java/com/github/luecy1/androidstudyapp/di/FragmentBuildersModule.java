package com.github.luecy1.androidstudyapp.di;

import com.github.luecy1.androidstudyapp.ui.repo.RepoFragment;
import com.github.luecy1.androidstudyapp.ui.search.SearchFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by you on 2018/01/29.
 */
@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract RepoFragment contibuteRepoFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

}
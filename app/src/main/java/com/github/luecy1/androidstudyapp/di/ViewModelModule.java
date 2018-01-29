package com.github.luecy1.androidstudyapp.di;

import android.arch.lifecycle.ViewModel;

import com.github.luecy1.androidstudyapp.ui.repo.RepoViewModel;
import com.github.luecy1.androidstudyapp.ui.search.SearchViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by you on 2018/01/29.
 */
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel searchViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RepoViewModel.class)
    abstract ViewModel bindRepoViewModel(RepoViewModel repoViewModel);

}

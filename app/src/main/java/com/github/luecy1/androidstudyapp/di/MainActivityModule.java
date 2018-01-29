package com.github.luecy1.androidstudyapp.di;

import com.github.luecy1.androidstudyapp.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by you on 2018/01/29.
 */
@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules =FragmentBuildersModule.class)
    abstract MainActivity contributeActivity();
}

package com.github.luecy1.androidstudyapp.di;


import com.github.luecy1.androidstudyapp.api.GithubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by you on 2018/01/29.
 */
@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    GithubService provideGithubService() {
        return null;
    }

}

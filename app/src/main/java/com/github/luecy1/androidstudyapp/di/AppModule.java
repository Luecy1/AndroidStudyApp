package com.github.luecy1.androidstudyapp.di;


import com.github.luecy1.androidstudyapp.api.GithubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by you on 2018/01/29.
 */
@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    GithubService provideGithubService() {

        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                // TODO
                .build()
                .create(GithubService.class);
    }

}

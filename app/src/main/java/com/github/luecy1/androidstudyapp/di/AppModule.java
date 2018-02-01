package com.github.luecy1.androidstudyapp.di;


import android.app.Application;
import android.arch.persistence.room.Room;

import com.github.luecy1.androidstudyapp.api.GithubService;
import com.github.luecy1.androidstudyapp.db.GithubDb;
import com.github.luecy1.androidstudyapp.db.RepoDao;
import com.github.luecy1.androidstudyapp.db.UserDao;
import com.github.luecy1.androidstudyapp.util.LiveDataCallAdapterFactory;

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
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(GithubService.class);
    }

    @Singleton
    @Provides
    GithubDb provideDb(Application app) {
        return Room.databaseBuilder(app, GithubDb.class, "github.db").build();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(GithubDb db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    RepoDao provideRepoDao(GithubDb db) {
        return db.repoDao();
    }
}

package com.github.luecy1.androidstudyapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.luecy1.androidstudyapp.AppExecutors;
import com.github.luecy1.androidstudyapp.api.ApiResponse;
import com.github.luecy1.androidstudyapp.api.GithubService;
import com.github.luecy1.androidstudyapp.db.UserDao;
import com.github.luecy1.androidstudyapp.vo.Resource;
import com.github.luecy1.androidstudyapp.vo.User;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by you on 2018/02/07.
 */
@Singleton
public class UserRepository {

    private final UserDao userDao;
    private final GithubService githubService;
    private final AppExecutors appExecutors;

    @Inject
    public UserRepository(UserDao userDao, GithubService githubService, AppExecutors appExecutors) {
        this.userDao = userDao;
        this.githubService = githubService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<User>> loadUser(String login) {
        return new NetworkBoundResource<User, User>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.findByLogin(login);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return githubService.getUser(login);
            }
        }.asLiveData();
    }
}

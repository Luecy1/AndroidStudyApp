package com.github.luecy1.androidstudyapp.ui.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.luecy1.androidstudyapp.repository.RepoRepository;
import com.github.luecy1.androidstudyapp.repository.UserRepository;
import com.github.luecy1.androidstudyapp.util.AbsentLiveData;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;
import com.github.luecy1.androidstudyapp.vo.User;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by you on 2018/02/07.
 */
public class UserViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<String> login = new MutableLiveData<>();
    private final LiveData<Resource<List<Repo>>> repositories;
    private final LiveData<Resource<User>> user;

    @SuppressWarnings("unchecked")
    @Inject
    public UserViewModel(UserRepository userRepository, RepoRepository repoRepository) {
        user = Transformations.switchMap(login, login -> {
           if (login == null) {
               return AbsentLiveData.create();
           } else {
               return userRepository.loadUser(login);
           }
        });
        repositories = Transformations.switchMap(login, login -> {
           if (login == null) {
               return AbsentLiveData.create();
           } else {
               return repoRepository.loadRepos(login);
           }
        });
    }

    @VisibleForTesting
    public void setLogin(String login) {
        if (Objects.equals(this.login.getValue(), login)) {
            return;
        }
        this.login.setValue(login);
    }

    @VisibleForTesting
    public LiveData<Resource<User>> getUser() {
        return user;
    }

    @VisibleForTesting
    public LiveData<Resource<List<Repo>>> getRepositories() {
        return repositories;
    }

    @VisibleForTesting
    public void retry() {
        if (this.login.getValue() != null) {
            this.login.setValue(this.login.getValue());
        }
    }
}

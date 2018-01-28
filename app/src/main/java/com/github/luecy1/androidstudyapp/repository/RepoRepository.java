package com.github.luecy1.androidstudyapp.repository;

import android.arch.lifecycle.LiveData;

import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;

import javax.inject.Singleton;

/**
 * Created by you on 2018/01/28.
 */

@Singleton
public class RepoRepository {

    public LiveData<Resource<Repo>> loadRepo(String owner, String name) {
        // TODO
        return null;
    }

    public LiveData<Resource<Repo>> search(String query) {
        // TODO
        return null;
    }
}

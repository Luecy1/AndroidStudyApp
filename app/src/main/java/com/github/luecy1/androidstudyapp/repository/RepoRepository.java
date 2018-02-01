package com.github.luecy1.androidstudyapp.repository;

import android.arch.lifecycle.LiveData;

import com.github.luecy1.androidstudyapp.AppExecutors;
import com.github.luecy1.androidstudyapp.api.GithubService;
import com.github.luecy1.androidstudyapp.db.GithubDb;
import com.github.luecy1.androidstudyapp.db.RepoDao;
import com.github.luecy1.androidstudyapp.util.RateLimiter;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by you on 2018/01/28.
 */

@Singleton
public class RepoRepository {

    private final GithubDb db;

    private final RepoDao repoDao;

    private final GithubService githubService;

    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListLimit = new RateLimiter<>(10, TimeUnit.MINUTES);


    @Inject
    public RepoRepository(AppExecutors appExecutors, GithubDb db, RepoDao repoDao,
                          GithubService githubService) {
        this.db = db;
        this.repoDao = repoDao;
        this.githubService = githubService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<Repo>>> loadRepos(String owner) {
        // TODO
        return null;
    }

    public LiveData<Resource<Repo>> loadRepo(String owner, String name) {
        // TODO
        return null;
    }


    public LiveData<Resource<Repo>> search(String query) {
        // TODO
        return null;
    }
}

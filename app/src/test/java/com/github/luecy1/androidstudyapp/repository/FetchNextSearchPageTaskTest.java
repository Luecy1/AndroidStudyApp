package com.github.luecy1.androidstudyapp.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import com.github.luecy1.androidstudyapp.api.GithubService;
import com.github.luecy1.androidstudyapp.db.GithubDb;
import com.github.luecy1.androidstudyapp.db.RepoDao;
import com.github.luecy1.androidstudyapp.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by you on 2018/02/15.
 */
// TODO
public class FetchNextSearchPageTaskTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GithubService service;

    private GithubDb db;

    private RepoDao repoDao;

    private FetchNextSearchPageTask task;

    private LiveData<Resource<Boolean>> value;

    private Observer<Resource<Boolean>> observer;

    @Before
    public void init() {
        service = mock(GithubService.class);
        db = mock(GithubDb.class);
        repoDao = mock(RepoDao.class);
        when(db.repoDao()).thenReturn(repoDao);
        task = new FetchNextSearchPageTask("foo", service, db);
        //noinspection unchecked
        observer = mock(Observer.class);
        task.getLiveData().observeForever(observer);
    }

    @Test
    public void withoutResult() {
        when(repoDao.search("foo")).thenReturn(null);
        task.run();
        verify(observer).onChanged(null);
        verifyNoMoreInteractions(observer);
        verifyNoMoreInteractions(service);
    }
}

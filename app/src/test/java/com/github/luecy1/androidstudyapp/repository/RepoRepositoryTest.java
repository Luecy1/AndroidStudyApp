package com.github.luecy1.androidstudyapp.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.github.luecy1.androidstudyapp.TestUtil;
import com.github.luecy1.androidstudyapp.api.ApiResponse;
import com.github.luecy1.androidstudyapp.api.GithubService;
import com.github.luecy1.androidstudyapp.api.RepoSearchResponse;
import com.github.luecy1.androidstudyapp.db.GithubDb;
import com.github.luecy1.androidstudyapp.db.RepoDao;
import com.github.luecy1.androidstudyapp.util.AbsentLiveData;
import com.github.luecy1.androidstudyapp.util.InstantAppExecutors;
import com.github.luecy1.androidstudyapp.vo.Contributor;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.RepoSearchResult;
import com.github.luecy1.androidstudyapp.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

import static com.github.luecy1.androidstudyapp.util.ApiUtil.successCall;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by you on 2018/02/12.
 */
@SuppressWarnings("unchecked")
@RunWith(JUnit4.class)
public class RepoRepositoryTest {

    private RepoRepository repository;
    private RepoDao dao;
    private GithubService service;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        dao = mock(RepoDao.class);
        service = mock(GithubService.class);
        GithubDb db = mock(GithubDb.class);
        when(db.repoDao()).thenReturn(dao);
        repository = new RepoRepository(new InstantAppExecutors(), db, dao, service);
    }

    @Test
    public void loadRepoFromNetwork() throws IOException {
        MutableLiveData<Repo> dbData = new MutableLiveData<>();
        when(dao.load("foo", "bar")).thenReturn(dbData);

        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        LiveData<ApiResponse<Repo>> call = successCall(repo);
        when(service.getRepo("foo", "bar")).thenReturn(call);

        LiveData<Resource<Repo>> data = repository.loadRepo("foo", "bar");
        verify(dao).load("foo", "bar");
        verifyNoMoreInteractions(service);

        Observer observer = mock(Observer.class);
        data.observeForever(observer);
        verifyNoMoreInteractions(service);
        verify(observer).onChanged(Resource.loading(null));
        MutableLiveData<Repo> updatedDbData = new MutableLiveData<>();
        when(dao.load("foo", "bar")).thenReturn(updatedDbData);

        dbData.postValue(null);
        verify(service).getRepo("foo", "bar");
        verify(dao).insert(repo);

        updatedDbData.postValue(repo);
        verify(observer).onChanged(Resource.success(repo));
    }

    @Test
    public void loadContributors() throws IOException {
        MutableLiveData<List<Contributor>> dbData = new MutableLiveData<>();
        when(dao.loadContributors("foo", "bar")).thenReturn(dbData);

        LiveData<Resource<List<Contributor>>> data = repository.loadContributors("foo",
                "bar");
        verify(dao).loadContributors("foo", "bar");

        verify(service, never()).getContributors(anyString(), anyString());

        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        Contributor contributor = TestUtil.createContributor(repo, "log", 3);
        // network does not send these
        contributor.setRepoOwner(null);
        contributor.setRepoName(null);
        List<Contributor> contributors = Collections.singletonList(contributor);
        LiveData<ApiResponse<List<Contributor>>> call = successCall(contributors);
        when(service.getContributors("foo", "bar"))
                .thenReturn(call);

        Observer<Resource<List<Contributor>>> observer = mock(Observer.class);
        data.observeForever(observer);

        MutableLiveData<List<Contributor>> updatedDbData = new MutableLiveData<>();
        when(dao.loadContributors("foo", "bar")).thenReturn(updatedDbData);
        dbData.setValue(Collections.emptyList());

        verify(service).getContributors("foo", "bar");
        ArgumentCaptor<List<Contributor>> inserted = ArgumentCaptor.forClass((Class) List.class);
        verify(dao).insertContributes(inserted.capture());


        assertThat(inserted.getValue().size(), is(1));
        Contributor first = inserted.getValue().get(0);
        assertThat(first.getRepoName(), is("bar"));
        assertThat(first.getRepoOwner(), is("foo"));

        updatedDbData.setValue(contributors);
        verify(observer).onChanged(Resource.success(contributors));
    }

    @Test
    public void searchNextPage_null() {
        when(dao.findSerchResult("foo")).thenReturn(null);
        Observer<Resource<Boolean>> observer = mock(Observer.class);
        repository.searchNextPage("foo").observeForever(observer);
        verify(observer).onChanged(null);
    }

    @Test
    public void search_fromDb() {
        List<Integer> ids = Arrays.asList(1, 2);

        Observer<Resource<List<Repo>>> observer = mock(Observer.class);
        MutableLiveData<RepoSearchResult> dbSearchResult = new MutableLiveData<>();
        MutableLiveData<List<Repo>> repositories = new MutableLiveData<>();

        when(dao.search("foo")).thenReturn(dbSearchResult);

        repository.search("foo").observeForever(observer);

        verify(observer).onChanged(Resource.loading(null));
        verifyNoMoreInteractions(service);
        reset(observer);

        RepoSearchResult dbResult = new RepoSearchResult("foo", ids, 2, null);
        when(dao.loadOrderd(ids)).thenReturn(repositories);

        dbSearchResult.postValue(dbResult);

        List<Repo> repoList = new ArrayList<>();
        repositories.postValue(repoList);
        verify(observer).onChanged(Resource.success(repoList));
        verifyNoMoreInteractions(service);
    }

    @Test
    public void search_fromServer() {
        List<Integer> ids = Arrays.asList(1, 2);
        Repo repo1 = TestUtil.createRepo(1, "owner", "repo 1", "desc 1");
        Repo repo2 = TestUtil.createRepo(2, "owner", "repo 2", "desc 2");

        Observer<Resource<List<Repo>>> observer = mock(Observer.class);
        MutableLiveData<RepoSearchResult> dbSearchResult = new MutableLiveData<>();
        MutableLiveData<List<Repo>> repositories = new MutableLiveData<>();

        RepoSearchResponse apiResponse = new RepoSearchResponse();
        List<Repo> repoList = Arrays.asList(repo1, repo2);
        apiResponse.setItems(repoList);
        apiResponse.setTotal(2);

        MutableLiveData<ApiResponse<RepoSearchResponse>> callLiveData = new MutableLiveData<>();
        when(service.searchRepos("foo")).thenReturn(callLiveData);

        when(dao.search("foo")).thenReturn(dbSearchResult);

        repository.search("foo").observeForever(observer);

        verify(observer).onChanged(Resource.loading(null));
        verifyNoMoreInteractions(service);
        reset(observer);

        when(dao.loadOrderd(ids)).thenReturn(repositories);
        dbSearchResult.postValue(null);
        verify(dao, never()).loadOrderd(anyObject());

        verify(service).searchRepos("foo");
        MutableLiveData<RepoSearchResult> updateResult = new MutableLiveData<>();
        when(dao.search("foo")).thenReturn(updateResult);
        updateResult.postValue(new RepoSearchResult("foo", ids, 2, null));

        callLiveData.postValue(new ApiResponse<>(Response.success(apiResponse)));
        verify(dao).insertRepos(repoList);
        repositories.postValue(repoList);
        verify(observer).onChanged(Resource.success(repoList));
        verifyNoMoreInteractions(service);
    }

    @Test
    public void search_fromServer_error() {
        when(dao.search("foo")).thenReturn(AbsentLiveData.create());
        MutableLiveData<ApiResponse<RepoSearchResponse>> apiResponse = new MutableLiveData<>();
        when(service.searchRepos("foo")).thenReturn(apiResponse);

        Observer<Resource<List<Repo>>> observer = mock(Observer.class);
        repository.search("foo").observeForever(observer);
        verify(observer).onChanged(Resource.loading(null));

        apiResponse.postValue(new ApiResponse<>(new Exception("idk")));
        verify(observer).onChanged(Resource.error("idk", null));
    }
}

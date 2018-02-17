package com.github.luecy1.androidstudyapp.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import com.github.luecy1.androidstudyapp.TestUtil;
import com.github.luecy1.androidstudyapp.api.GithubService;
import com.github.luecy1.androidstudyapp.api.RepoSearchResponse;
import com.github.luecy1.androidstudyapp.db.GithubDb;
import com.github.luecy1.androidstudyapp.db.RepoDao;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.RepoSearchResult;
import com.github.luecy1.androidstudyapp.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by you on 2018/02/15.
 */
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

    @Test
    public void onNextPage() {
        createDbResult(null);
        task.run();
        verify(observer).onChanged(Resource.success(false));
        verifyNoMoreInteractions(observer);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void nextPageWithNull() throws IOException {
        createDbResult(1);
        RepoSearchResponse result = new RepoSearchResponse();
        result.setTotal(10);
        List<Repo> repos = TestUtil.createRepos(10, "a", "b", "c");
        result.setItems(repos);
        Call<RepoSearchResponse> call = createCall(result, null);
        when(service.searchRepos("foo", 1)).thenReturn(call);
        task.run();
        verify(repoDao).insertRepos(repos);
        verify(observer).onChanged(Resource.success(false));
    }

    @Test
    public void nextPageWithMore() throws IOException {
        createDbResult(1);
        RepoSearchResponse result = new RepoSearchResponse();
        result.setTotal(10);
        List<Repo> repos = TestUtil.createRepos(10, "a", "b", "c");
        result.setItems(repos);
        result.setNextPage(2);
        Call<RepoSearchResponse> call = createCall(result, 2);
        when(service.searchRepos("foo", 1)).thenReturn(call);
        task.run();
        verify(repoDao).insertRepos(repos);
        verify(observer).onChanged(Resource.success(true));
    }

    @Test
    public void nextPageApiError() throws IOException {
        createDbResult(1);
        Call<RepoSearchResponse> call = mock(Call.class);
        when(call.execute()).thenReturn(Response.error(400, ResponseBody.create(
                MediaType.parse("txt"), "bar")));
        when(service.searchRepos("foo", 1)).thenReturn(call);
        task.run();
        verify(observer).onChanged(Resource.error("bar", true));
    }

    @Test
    public void nextPageIOError() throws IOException {
        createDbResult(1);
        Call<RepoSearchResponse> call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("bar"));
        when(service.searchRepos("foo", 1)).thenReturn(call);
        task.run();
        verify(observer).onChanged(Resource.error("bar", true));
    }

    private Call<RepoSearchResponse> createCall(RepoSearchResponse body, Integer nextPage) throws IOException {
        Headers headers = nextPage == null ? null : Headers
                .of("link", "<https://api.github.com/search/repositories?q=foo&page=" + nextPage
                        + ">; rel=\"next\"");
        Response<RepoSearchResponse> success = headers == null ?
                Response.success(body) : Response.success(body, headers);
        Call call = mock(Call.class);
        when(call.execute()).thenReturn(success);

        return call;
    }

    private void createDbResult(Integer nextPage) {
        RepoSearchResult result = new RepoSearchResult("foo", Collections.emptyList(),
                0, nextPage);
        when(repoDao.findSerchResult("foo")).thenReturn(result);
    }
}

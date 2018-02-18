package com.github.luecy1.androidstudyapp.ui.search;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.github.luecy1.androidstudyapp.repository.RepoRepository;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by you on 2018/02/18.
 */
// TODO
@RunWith(JUnit4.class)
public class SearchViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private SearchViewModel viewModel;
    private RepoRepository repository;

    @Before
    public void init() {
        repository = mock(RepoRepository.class);
        viewModel = new SearchViewModel(repository);
    }

    @Test
    public void empty() {
        Observer<Resource<List<Repo>>> result = mock(Observer.class);
        viewModel.getResults().observeForever(result);
        viewModel.loadNextPage();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void basic() {
        Observer<Resource<List<Repo>>> result = mock(Observer.class);
        viewModel.getResults().observeForever(result);
        viewModel.setQuery("foo");
        verify(repository).search("foo");
        verify(repository, never()).searchNextPage("foo");
    }

    @Test
    public void noObserverNoQuery() {
        when(repository.searchNextPage("foo")).thenReturn(mock(LiveData.class));
        viewModel.setQuery("foo");
        verify(repository, never()).search("foo");

        viewModel.loadNextPage();
        verify(repository).searchNextPage("foo");
    }

    @Test
    public void swap() {
        LiveData<Resource<Boolean>> nextPage = new MutableLiveData<>();
        when(repository.searchNextPage("foo")).thenReturn(nextPage);

        Observer<Resource<List<Repo>>> result = mock(Observer.class);
        viewModel.getResults().observeForever(result);
        verifyNoMoreInteractions(repository);
        viewModel.setQuery("foo");
        verify(repository).search("foo");
        viewModel.loadNextPage();

        viewModel.getLoadMoreStatus().observeForever(mock(Observer.class));
        verify(repository).searchNextPage("foo");
        assertThat(nextPage.hasActiveObservers(),is(true));
        viewModel.setQuery("bar");
        assertThat(nextPage.hasActiveObservers(), is(false));
        verify(repository).search("bar");
        verify(repository, never()).searchNextPage("bar");
    }
}

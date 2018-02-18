package com.github.luecy1.androidstudyapp.ui.repo;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.github.luecy1.androidstudyapp.repository.RepoRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by you on 2018/02/19.
 */
// TODO
@RunWith(JUnit4.class)
public class RepoViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RepoRepository repository;
    private RepoViewModel repoViewModel;

    @Before
    public void setUp() throws Exception {
        repository = mock(RepoRepository.class);
        repoViewModel = new RepoViewModel(repository);
    }

    @Test
    public void testNull() {
        assertThat(repoViewModel.getRepo(), notNullValue());
        assertThat(repoViewModel.getContributors(), notNullValue());
        verify(repository, never()).loadRepo(anyString(), anyString());
    }
}

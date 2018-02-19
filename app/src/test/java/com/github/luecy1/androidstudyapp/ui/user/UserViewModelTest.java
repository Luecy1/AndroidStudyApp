package com.github.luecy1.androidstudyapp.ui.user;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.github.luecy1.androidstudyapp.repository.RepoRepository;
import com.github.luecy1.androidstudyapp.repository.UserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by you on 2018/02/20.
 */
// TODO
@SuppressWarnings("unchecked")
@RunWith(JUnit4.class)
public class UserViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private UserViewModel userViewModel;
    private UserRepository userRepository;
    private RepoRepository repoRepository;

    @Before
    public void setup() {
        userRepository = mock(UserRepository.class);
        repoRepository = mock(RepoRepository.class);
        userViewModel = new UserViewModel(userRepository, repoRepository);
    }

    @Test
    public void testNull() {
        assertThat(userViewModel.getUser(), notNullValue());
        verify(userRepository, never()).loadUser(anyString());
        userViewModel.setLogin("foo");
        verify(userRepository, never()).loadUser(anyString());
    }
}

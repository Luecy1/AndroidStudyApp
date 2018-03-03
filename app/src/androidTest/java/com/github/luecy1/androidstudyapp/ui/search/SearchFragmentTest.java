package com.github.luecy1.androidstudyapp.ui.search;


import android.arch.lifecycle.MutableLiveData;
import android.databinding.DataBindingComponent;
import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.view.View;

import com.github.luecy1.androidstudyapp.R;
import com.github.luecy1.androidstudyapp.TestUtil;
import com.github.luecy1.androidstudyapp.binding.FragmentBindingAdapters;
import com.github.luecy1.androidstudyapp.testing.SingleFragmentActivity;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;
import com.github.luecy1.androidstudyapp.util.EspressoTestUtil;
import com.github.luecy1.androidstudyapp.util.RecyclerViewMatcher;
import com.github.luecy1.androidstudyapp.util.TaskExecutorWithIdlingResourceRule;
import com.github.luecy1.androidstudyapp.util.ViewModelUtil;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by you on 2018/02/17.
 */
@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {
    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);
    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    private FragmentBindingAdapters fragmentBindingAdapters;
    private NavigationController navigationController;

    private SearchViewModel viewModel;

    private MutableLiveData<Resource<List<Repo>>> results = new MutableLiveData<>();
    private MutableLiveData<SearchViewModel.LoadMoreState> loadMoreStatus = new MutableLiveData<>();

    @Before
    public void init() {
        EspressoTestUtil.disableProgressBarAnimations(activityRule);
        SearchFragment searchFragment = new SearchFragment();
        viewModel = mock(SearchViewModel.class);
        doReturn(loadMoreStatus).when(viewModel).getLoadMoreStatus();
        when(viewModel.getResults()).thenReturn(results);

        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);
        navigationController = mock(NavigationController.class);
        searchFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
//        searchFragment.dataBindingComponent = () -> fragmentBindingAdapters;
        searchFragment.dataBindingComponent = new DataBindingComponent(){
            @Override
            public FragmentBindingAdapters getFragmentBindingAdapters() {
                return fragmentBindingAdapters;
            }
        };
        searchFragment.navigationController = navigationController;
        activityRule.getActivity().setFragment(searchFragment);
    }

    @Test
    public void search() {
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.input)).perform(typeText("foo"),
                pressKey(KeyEvent.KEYCODE_ENTER));
        verify(viewModel).setQuery("foo");
        results.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
    }

    @Test
    public void loadResults() {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        results.postValue(Resource.loading(Arrays.asList(repo)));
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("foo/bar"))));
        verify(viewModel).loadNextPage();
    }

    @Test
    public void navigateToRepo() {
        doNothing().when(viewModel).loadNextPage();
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        results.postValue(Resource.success(Arrays.asList(repo)));
        onView(withText("desc")).perform(click());
        verify(navigationController).navigateToRepo("foo", "bar");
    }

    @Test
    public void loadMoreProgress() {
        loadMoreStatus.postValue(new SearchViewModel.LoadMoreState(true, null));
        onView(withId(R.id.load_more_bar)).check(matches(isDisplayed()));
        loadMoreStatus.postValue(new SearchViewModel.LoadMoreState(false,null));
        onView(withId(R.id.load_more_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void loadMoreProgressError() {
        loadMoreStatus.postValue(new SearchViewModel.LoadMoreState(true, "QQ"));
        onView(withText("QQ")).check(matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        ));
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.repo_list);
    }
}

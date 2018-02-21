package com.github.luecy1.androidstudyapp.ui.search;


import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import com.github.luecy1.androidstudyapp.R;
import com.github.luecy1.androidstudyapp.binding.FragmentBindingAdapters;
import com.github.luecy1.androidstudyapp.testing.SingleFragmentActivity;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;
import com.github.luecy1.androidstudyapp.util.EspressoTestUtil;
import com.github.luecy1.androidstudyapp.util.TaskExecutorWithIdlingResourceRule;
import com.github.luecy1.androidstudyapp.util.ViewModelUtil;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by you on 2018/02/17.
 */
// TODO　週末に回す
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
        // TODO
//        searchFragment.dataBindingComponent = () -> fragmentBindingAdapters;
        searchFragment.navigationController = navigationController;
        activityRule.getActivity().setFragment(searchFragment);
    }

    @Test
    public void searchA() {
//        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.input)).perform(typeText("foo"),
//                pressKey(KeyEvent.KEYCODE_ENTER));
//        verify(viewModel).setQuery("foo");
//        results.postValue(Resource.loading(null));
//        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void loadResult() {
    }
}

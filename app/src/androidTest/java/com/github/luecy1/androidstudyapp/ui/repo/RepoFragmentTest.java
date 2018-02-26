package com.github.luecy1.androidstudyapp.ui.repo;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.luecy1.androidstudyapp.R;
import com.github.luecy1.androidstudyapp.binding.FragmentBindingAdapters;
import com.github.luecy1.androidstudyapp.testing.SingleFragmentActivity;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;
import com.github.luecy1.androidstudyapp.util.EspressoTestUtil;
import com.github.luecy1.androidstudyapp.util.TaskExecutorWithIdlingResourceRule;
import com.github.luecy1.androidstudyapp.util.ViewModelUtil;
import com.github.luecy1.androidstudyapp.vo.Contributor;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by you on 2018/02/24.
 */
// TODO
@RunWith(AndroidJUnit4.class)
public class RepoFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<SingleFragmentActivity>(SingleFragmentActivity.class, true);
    @Rule
    public TaskExecutorWithIdlingResourceRule executorrule =
            new TaskExecutorWithIdlingResourceRule();
    private MutableLiveData<Resource<Repo>> repo = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Contributor>>> contributors = new MutableLiveData<>();
    private RepoFragment repoFragment;
    private RepoViewModel viewModel;

    private FragmentBindingAdapters fragmentBindingAdapters;
    private NavigationController navigationController;

    @Before
    public void init() {
        EspressoTestUtil.disableProgressBarAnimations(activityRule);
        repoFragment = RepoFragment.create("a","b ");
        viewModel = mock(RepoViewModel.class);
        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);
        navigationController = mock(NavigationController.class);
        doNothing().when(viewModel).setId(anyString(), anyString());
        when(viewModel.getRepo()).thenReturn(repo);
        when(viewModel.getContributors()).thenReturn(contributors);

        repoFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        repoFragment.dataBindingComponent = () -> fragmentBindingAdapters;
        repoFragment.navigationController = navigationController;
        activityRule.getActivity().setFragment(repoFragment);
    }

    @Test
    public void testLoading() {
        repo.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
    }
}

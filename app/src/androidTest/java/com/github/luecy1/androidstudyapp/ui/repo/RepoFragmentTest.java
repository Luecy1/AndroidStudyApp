package com.github.luecy1.androidstudyapp.ui.repo;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.DataBindingComponent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.luecy1.androidstudyapp.R;
import com.github.luecy1.androidstudyapp.TestUtil;
import com.github.luecy1.androidstudyapp.binding.FragmentBindingAdapters;
import com.github.luecy1.androidstudyapp.binding.FragmentDataBindingComponent;
import com.github.luecy1.androidstudyapp.testing.SingleFragmentActivity;
import com.github.luecy1.androidstudyapp.ui.common.NavigationController;
import com.github.luecy1.androidstudyapp.util.EspressoTestUtil;
import com.github.luecy1.androidstudyapp.util.RecyclerViewMatcher;
import com.github.luecy1.androidstudyapp.util.TaskExecutorWithIdlingResourceRule;
import com.github.luecy1.androidstudyapp.util.ViewModelUtil;
import com.github.luecy1.androidstudyapp.vo.Contributor;
import com.github.luecy1.androidstudyapp.vo.Repo;
import com.github.luecy1.androidstudyapp.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by you on 2018/02/24.
 */
@RunWith(AndroidJUnit4.class)
public class RepoFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);
    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
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
        repoFragment = RepoFragment.create("a", "b");
        viewModel = mock(RepoViewModel.class);
        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);
        navigationController = mock(NavigationController.class);
        doNothing().when(viewModel).setId(anyString(), anyString());
        when(viewModel.getRepo()).thenReturn(repo);
        when(viewModel.getContributors()).thenReturn(contributors);

        repoFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
//        repoFragment.dataBindingComponent = () -> fragmentBindingAdapters;
        repoFragment.dataBindingComponent = new DataBindingComponent() {
            @Override
            public FragmentBindingAdapters getFragmentBindingAdapters() {
                return RepoFragmentTest.this.fragmentBindingAdapters;
            }
        };
        repoFragment.navigationController = navigationController;
        activityRule.getActivity().setFragment(repoFragment);
    }

    @Test
    public void testLoading() {
        repo.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testValueWhileLoading() {
        Repo repo = TestUtil.createRepo("yigit", "foo", "foo-bar");
        this.repo.postValue(Resource.loading(repo));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.name)).check(matches(
                withText(getString(R.string.repo_full_name, "yigit", "foo"))
        ));
        onView(withId(R.id.description)).check(matches(withText("foo-bar")));
    }

    @Test
    public void testError() throws InterruptedException {
        repo.postValue(Resource.error("foo", null));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).perform(click());
        verify(viewModel).retry();
        repo.postValue(Resource.loading(null));

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
        Repo repo = TestUtil.createRepo("owner", "name", "desc");
        this.repo.postValue(Resource.success(repo));

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
        onView(withId(R.id.name)).check(matches(
                withText(getString(R.string.repo_full_name, "owner", "name"))
        ));
        onView(withId(R.id.description)).check(matches(withText("desc")));
    }

    @Test
    public void testContributors() {
        setContributors("aa", "bb");
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withText("aa"))));
        onView(listMatcher().atPosition(1))
                .check(matches(hasDescendant(withText("bb"))));
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.contributor_list);
    }

    private void setContributors(String... names) {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        List<Contributor> contributors = new ArrayList<>();
        int contributionCount = 100;
        for (String name : names) {
            contributors.add(TestUtil.createContributor(repo, name, contributionCount--));
        }
        this.contributors.postValue(Resource.success(contributors));
    }

    private String getString(@StringRes int id, Object... args) {
        return InstrumentationRegistry.getTargetContext().getString(id, args);
    }
}
